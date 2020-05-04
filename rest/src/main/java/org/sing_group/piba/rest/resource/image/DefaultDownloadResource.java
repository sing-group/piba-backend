/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato,
 * 			Florentino Fdez-Riverola, Alba Nogueira Rodríguez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.piba.rest.resource.image;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.sing_group.piba.domain.entities.image.ImageFilter;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.image.DownloadResource;
import org.sing_group.piba.service.entity.image.ImagesInGallery;
import org.sing_group.piba.service.spi.image.GalleryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("download")
@Api(value = "download")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultDownloadResource implements DownloadResource {

  @Inject
  private GalleryService service;

  @GET
  @Path("/gallery/{id}")
  @Produces("application/zip")
  @ApiOperation(
    value = "Return the images of a gallery in a zip.", response = StreamingOutput.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown gallery: {id}"),
    @ApiResponse(code = 400, message = "Unknown filter: {filter}"),
    @ApiResponse(code = 400, message = "Unknown location: {location}")
  })
  @Override
  public Response getGalleryInZip(
    @PathParam("id") String id,
    @QueryParam("filter") ImageFilter filter,
    @QueryParam("withLocation") boolean withLocation
  ) throws FileNotFoundException {
    final String withLocationName = withLocation ? "_with_location" : "_without_location";
    
    final ImagesInGallery galleriesInZip = this.service.getGalleriesInZip(this.service.get(id), filter, withLocation);
    final StreamingOutput result = new StreamingOutput() {
      @Override
      public void write(OutputStream output) throws IOException, WebApplicationException {
        galleriesInZip.writeTo(output);
      }
    };
    
    return Response.ok(result)
      .header("Content-Disposition", "attachment; filename=\"" + id + "_" + filter + withLocationName + ".zip\"")
    .build();
  }

}
