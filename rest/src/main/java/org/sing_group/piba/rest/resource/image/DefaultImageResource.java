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
import java.util.function.IntSupplier;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.sing_group.piba.domain.entities.image.Gallery;
import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.ImageFilter;
import org.sing_group.piba.domain.entities.image.PolypLocation;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.rest.entity.RestImageUploadData;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.image.ImageData;
import org.sing_group.piba.rest.entity.image.PolypLocationData;
import org.sing_group.piba.rest.entity.image.PolypLocationEditionData;
import org.sing_group.piba.rest.entity.mapper.spi.ImageMapper;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.image.ImageResource;
import org.sing_group.piba.service.spi.image.GalleryService;
import org.sing_group.piba.service.spi.image.ImageService;
import org.sing_group.piba.service.spi.polyp.PolypService;
import org.sing_group.piba.service.spi.storage.FileStorage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@RolesAllowed({
  "ADMIN", "USER", "ENDOSCOPIST"
})
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
@CrossDomain(allowedHeaders = "X-Pagination-Total-Items")
public class DefaultImageResource implements ImageResource {

  @Inject
  private ImageService service;

  @Inject
  private GalleryService galleryService;
  
  @Inject
  private PolypService polypService;

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

  @Path("{id:[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}}")
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

  @Path("{id:[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}}/metadata")
  @GET
  @ApiOperation(
    value = "Return the data of a image.", response = ImageData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown image: {id}")
  )
  @Override
  public Response getImage(@PathParam("id") String id) {
    Image img = this.service.get(id);
    if (img.isRemoved()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    return Response.ok(this.imageMapper.toImageData(this.service.get(id))).build();
  }

  @Path("{id:[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}}/polyplocation")
  @POST
  @RolesAllowed("ENDOSCOPIST")
  @ApiOperation(
    value = "Creates a polyp location in the image.", response = PolypLocationData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown image: {id}")
  )
  @Override
  public Response createPolypLocation(@PathParam("id") String id, PolypLocationEditionData polypLocationEditionData) {
    final PolypLocation polypLocation = new PolypLocation(
      polypLocationEditionData.getX(), polypLocationEditionData.getY(),
      polypLocationEditionData.getWidth(), polypLocationEditionData.getHeight(),
      this.service.get(id)
    );
    
    return Response.ok(this.imageMapper.toPolypLocationData(this.service.createPolypLocation(polypLocation))).build();
  }

  @Path("{id:[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}}/polyplocation")
  @PUT
  @RolesAllowed("ENDOSCOPIST")
  @ApiOperation(
    value = "Creates a polyp location in the image.", response = PolypLocationData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown image: {id}")
  )
  @Override
  public Response modifyPolypLocation(@PathParam("id") String id, PolypLocationEditionData polypLocationEditionData) {
    final PolypLocation polypLocation = this.service.getPolypLocation(this.service.get(id));
    
    polypLocation.setX(polypLocationEditionData.getX());
    polypLocation.setY(polypLocationEditionData.getY());
    polypLocation.setHeight(polypLocationEditionData.getHeight());
    polypLocation.setWidth(polypLocationEditionData.getWidth());
    
    return Response.ok(this.imageMapper.toPolypLocationData(this.service.modifyPolypLocation(polypLocation))).build();
  }

  @DELETE
  @Path("{id:[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}}")
  @RolesAllowed("ENDOSCOPIST")
  @ApiOperation(
    value = "Deletes an existing image.", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown image: {id}")
  )
  @Override
  public Response delete(@PathParam("id") String id, @HeaderParam("X-ReasonToRemove") String observationToRemove) {
    Image image = this.service.get(id);
    image.setObservationToRemove(observationToRemove);
    this.service.delete(image);
    return Response.ok().build();
  }

  @Path("{id:[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}}/polyplocation")
  @GET
  @ApiOperation(
    value = "Return the data of the polyp location in the image.", response = PolypLocationData.class, code = 200
  )
  @ApiResponses(value = {
    @ApiResponse(code = 400, message = "Unknown image: {id}"),
    @ApiResponse(code = 400, message = "No location for image: {id}")
  })
  @Override
  public Response getPolypLocation(@PathParam("id") String id) {
    Image image = this.service.get(id);
    return Response.ok(this.imageMapper.toPolypLocationData(this.service.getPolypLocation(image))).build();
  }

  @DELETE
  @Path("{id:[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}}/polyplocation")
  @RolesAllowed("ENDOSCOPIST")
  @ApiOperation(
    value = "Deletes an existing polyp location in the image.", code = 200
  )
  @ApiResponses(value = {
    @ApiResponse(code = 400, message = "Unknown image: {id}"),
    @ApiResponse(code = 400, message = "No location for image: {id}")
  })
  @Override
  public Response deletePolypLocation(@PathParam("id") String id) {
    this.service.deletePolypLocation(this.service.get(id));
    return Response.ok().build();
  }

  @GET
  @ApiOperation(
    value = "Returns a list of images according to the pagination and filter indicated", response = ImageData.class, code = 200
  )
  @ApiResponses(value = {
    @ApiResponse(code = 400, message = "Unknown gallery: {galleryId}"),
    @ApiResponse(code = 400, message = "Invalid page: {page} or pageSize: {pageSize}")
  })
  @Override
  public Response listImagesBy(
    @QueryParam("galleryId") String galleryId,
    @QueryParam("polypId") String polypId,
    @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
    @QueryParam("filter") ImageFilter filter
  ) {
    final boolean hasGalleryId = galleryId != null && !galleryId.isEmpty();
    final boolean hasPolypId = polypId != null && !polypId.isEmpty();
    
    final Stream<Image> imageList;
    final IntSupplier paginationTotalItems;
    
    if (hasGalleryId && hasPolypId) {
      final Polyp polyp = this.polypService.getPolyp(polypId);
      final Gallery gallery = this.galleryService.get(galleryId);
      imageList = this.service.listImagesByPolypAndGallery(polyp, gallery, page, pageSize, filter);
      paginationTotalItems = () -> this.service.countImagesByPolypAndGallery(polyp, gallery, filter);
    } else if (!hasGalleryId && !hasPolypId) {
      throw new IllegalArgumentException("galleryId or polypId must be provided");
    } else if (hasGalleryId) {
      final Gallery gallery = this.galleryService.get(galleryId);
      imageList = this.service.listImagesByGallery(gallery, page, pageSize, filter);
      paginationTotalItems = () -> this.service.countImagesByGallery(gallery, filter);
    } else {
      final Polyp polyp = this.polypService.getPolyp(polypId);
      imageList = this.service.listImagesByPolyp(polyp, page, pageSize, filter);
      paginationTotalItems = () -> this.service.countImagesByPolyp(polyp, filter);
    }
    
    ResponseBuilder response = Response.ok(
      imageList.map(this.imageMapper::toImageData).toArray(ImageData[]::new)
    );
    
    if (page != null && pageSize != null) {
      response = response.header("X-Pagination-Total-Items", paginationTotalItems.getAsInt());
    }
    
    return response.build();
  }

  @GET
  @Path("id")
  @ApiOperation(
    value = "Returns a list of identifiers and url to the resource based on the indicated filter", response = UuidAndUri.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown gallery: {galleryId}")
  )
  @Override
  public Response listImagesIdentifiersBy(
    @QueryParam("galleryId") String galleryId,
    @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
    @QueryParam("filter") ImageFilter filter
  ) {
    final Gallery gallery = this.galleryService.get(galleryId);

    ResponseBuilder response =
      Response.ok(
        this.service.listImagesIdentifiersByGallery(gallery, page, pageSize, filter)
          .map(this.imageMapper::toUuidAndUri).toArray(UuidAndUri[]::new)
      );
    
    if (page != null && pageSize != null) {
      response = response.header("X-Pagination-Total-Items", this.service.countImagesByGallery(gallery, filter));
    }
    
    return response.build();
  }

  @GET
  @Path("observations")
  @ApiOperation(
    value = "Returns a list of posible observations to remove", response = String.class, code = 200
  )
  @Override
  public Response listImageObservationsToRemoveBy(
    @QueryParam("observationStartsWith") String observationToRemoveStartsWith
  ) {
    return Response
      .ok(this.service.listImageObservationsToRemoveBy(observationToRemoveStartsWith).toArray(String[]::new))
    .build();
  }
}
