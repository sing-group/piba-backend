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
package org.sing_group.piba.rest.entity.mapper;

import static org.sing_group.piba.rest.entity.UuidAndUri.fromEntity;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.PolypLocation;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.image.ImageData;
import org.sing_group.piba.rest.entity.image.PolypLocationData;
import org.sing_group.piba.rest.entity.mapper.spi.ImageMapper;
import org.sing_group.piba.rest.resource.image.DefaultGalleryResource;
import org.sing_group.piba.rest.resource.image.DefaultImageResource;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;

@Default
public class DefaultImageMapper implements ImageMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public ImageData toImageData(Image image) {
    return new ImageData(
      image.getId(), image.getNumFrame(), image.isRemoved(),
      fromEntity(requestURI, image.getGallery(), DefaultGalleryResource.class),
      fromEntity(requestURI, image.getVideo(), DefaultVideoResource.class),
      image.getPolypLocation() == null ? null
        : fromEntity(requestURI, image, DefaultImageResource.class, "polyplocation")
    );
  }

  @Override
  public PolypLocationData toPolypLocationData(PolypLocation polypLocation) {
    return new PolypLocationData(
      polypLocation.getX(), polypLocation.getY(), polypLocation.getWidth(),
      polypLocation.getHeight()
    );
  }

  @Override
  public UuidAndUri toUuidAndUri(String identifier) {
    return new UuidAndUri(
      identifier,
      requestURI.getBaseUriBuilder().path(
        UriBuilder.fromResource(DefaultImageResource.class).path(identifier).path("metadata").build().toString()
      ).build()
    );
  }

}
