/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato,
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

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.image.Gallery;
import org.sing_group.piba.rest.entity.image.GalleryData;
import org.sing_group.piba.rest.entity.image.GalleryEditionData;
import org.sing_group.piba.rest.entity.mapper.spi.GalleryMapper;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.image.GalleryResource;
import org.sing_group.piba.service.spi.image.GalleryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RolesAllowed({
  "ADMIN", "USER", "ENDOSCOPIST"
})
@Path("gallery")
@Api(value = "gallery")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultGalleryResource implements GalleryResource {

  @Inject
  private GalleryService service;

  @Inject
  private GalleryMapper galleryMapper;

  @Context
  private UriInfo uriInfo;

  @POST
  @ApiOperation(
    value = "Creates a new gallery.", response = GalleryData.class, code = 201
  )
  @Override
  public Response create(GalleryEditionData galleryEditionData) {
    Gallery gallery =
      this.service.create(new Gallery(galleryEditionData.getTitle(), galleryEditionData.getDescription()));

    return Response.created(UriBuilder.fromResource(DefaultGalleryResource.class).path(gallery.getId()).build())
      .entity(galleryMapper.toGalleryData(gallery)).build();
  }

  @GET
  @ApiOperation(
    value = "Return the data of all galleries.", response = GalleryData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response getGalleries() {
    return Response.ok(this.service.getGalleries().map(this.galleryMapper::toGalleryData).toArray(GalleryData[]::new))
      .build();
  }

  @Path("{id}")
  @GET
  @ApiOperation(
    value = "Return the data of a gallery.", response = GalleryData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown gallery: {id}")
  )
  @Override
  public Response getGallery(@PathParam("id") String id) {
    return Response.ok(this.galleryMapper.toGalleryData(this.service.get(id))).build();
  }

  @PUT
  @RolesAllowed("ADMIN")
  @Path("{id}")
  @ApiOperation(
    value = "Modifies an existing gallery", response = GalleryData.class, code = 200
  )
  @ApiResponses(@ApiResponse(code = 400, message = "Unknown gallery: {id}"))
  @Override
  public Response edit(@PathParam("id") String id, GalleryEditionData galleryEditionData) {
    Gallery gallery = this.service.get(id);
    this.galleryMapper.assignGalleryEditionData(gallery, galleryEditionData);
    return Response.ok(this.galleryMapper.toGalleryData(this.service.edit(gallery)))
      .build();
  }

}
