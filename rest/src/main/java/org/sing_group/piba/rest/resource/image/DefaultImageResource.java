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

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.PolypLocation;
import org.sing_group.piba.rest.entity.RestImageUploadData;
import org.sing_group.piba.rest.entity.image.ImageData;
import org.sing_group.piba.rest.entity.image.PolypLocationData;
import org.sing_group.piba.rest.entity.image.PolypLocationEditionData;
import org.sing_group.piba.rest.entity.mapper.spi.ImageMapper;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.image.ImageResource;
import org.sing_group.piba.service.spi.image.ImageService;
import org.sing_group.piba.service.spi.storage.FileStorage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("image")
@Api(value = "image")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultImageResource implements ImageResource {

  @Inject
  private ImageService service;

  @Inject
  private ImageMapper imageMapper;

  @Inject
  private FileStorage fileStorage;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.imageMapper.setRequestURI(this.uriInfo);
  }

  @POST
  @ApiOperation(
    value = "Creates a new image.", responseHeaders = @ResponseHeader(
      name = "Location", description = "Location of the new image created.", response = ImageData.class
    ), code = 201
  )
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Override
  public Response uploadImage(RestImageUploadData restImageUploadData) {
    Image image = this.service.create(restImageUploadData);
    return Response.created(UriBuilder.fromResource(DefaultImageResource.class).path(image.getId()).build())
      .entity(imageMapper.toImageData(image)).build();
  }

  @Path("{id}")
  @GET
  @ApiOperation(
    value = "Return the bytes of a image.", response = byte[].class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown image: {id}")
  )
  @Override
  public Response getBytes(@PathParam("id") String id) {
    Set<String> formats = this.fileStorage.getFormatsForId(id);

    if (formats.size() == 0) {
      throw new IllegalArgumentException("Unknown image: " + id);
    }
    String format = formats.iterator().next();
    final InputStream imageStream = this.fileStorage.retrieve(id, format);
    try {
      return Response.ok(IOUtils.toByteArray(imageStream))
        .header("Content-Type", "image/" + format)
        .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Path("{id}/metadata")
  @GET
  @ApiOperation(
    value = "Return the data of a image.", response = ImageData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown image: {id}")
  )
  @Override
  public Response getImage(@PathParam("id") String id) {
    return Response.ok(this.imageMapper.toImageData(this.service.get(id))).build();
  }

  @Path("{id}/polyplocation")
  @POST
  @ApiOperation(
    value = "Creates a polyp location in the image.", response = PolypLocationData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown image: {id}")
  )
  @Override
  public Response createPolypLocation(@PathParam("id") String id, PolypLocationEditionData polypLocationEditicionData) {
    PolypLocation polypLocation =
      new PolypLocation(
        polypLocationEditicionData.getX(), polypLocationEditicionData.getY(), polypLocationEditicionData.getWidth(),
        polypLocationEditicionData.getHeight(), this.service.get(id)
      );
    return Response.ok(this.imageMapper.toPolypLocationData(this.service.createPolypLocation(polypLocation))).build();
  }

  @Path("{id}/polyplocation")
  @GET
  @ApiOperation(
    value = "Return the data of the polyp location in the image.", response = PolypLocationData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown image: {id}")
  )
  @Override
  public Response getPolypLocation(@PathParam("id") String id) {
    Image image = this.service.get(id);
    return Response.ok(this.imageMapper.toPolypLocationData(this.service.getPolypLocation(image))).build();
  }

}
