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

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.piba.domain.entities.image.Gallery;
import org.sing_group.piba.rest.entity.image.GalleryData;
import org.sing_group.piba.rest.entity.image.GalleryEditionData;
import org.sing_group.piba.rest.entity.mapper.spi.GalleryMapper;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.image.GalleryResource;
import org.sing_group.piba.service.spi.image.GalleryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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

}
