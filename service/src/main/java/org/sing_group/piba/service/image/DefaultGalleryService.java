/*-
 * #%L
 * Service
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
package org.sing_group.piba.service.image;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.image.GalleryDAO;
import org.sing_group.piba.domain.entities.image.Gallery;
import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.ImageFilter;
import org.sing_group.piba.service.entity.image.ImagesInGallery;
import org.sing_group.piba.service.spi.image.GalleryService;
import org.sing_group.piba.service.spi.image.ImageService;
import org.sing_group.piba.service.spi.storage.FileStorage;

@Stateless
@PermitAll
public class DefaultGalleryService implements GalleryService {

  @Inject
  private GalleryDAO galleryDao;

  @Inject
  private FileStorage fileStorage;

  @Inject
  private ImageService imageService;

  @Override
  public Gallery create(Gallery gallery) {
    return galleryDao.create(gallery);
  }

  @Override
  public Gallery get(String id) {
    return galleryDao.get(id);
  }

  @Override
  public Stream<Gallery> listGalleries() {
    return galleryDao.listGalleries();
  }

  @Override
  public ImagesInGallery getGalleriesInZip(Gallery gallery, ImageFilter filter, boolean withLocation)
    throws FileNotFoundException {
    final Stream<Image> images = this.imageService.listImagesByGallery(gallery, null, null, filter);

    if (withLocation) {
      return new ImagesInGallery(
        images,
        image -> this.fileStorage.retrieve(image.getId(), "png"),
        image -> this.imageService.getPolypLocationIfPresent(image)
      );
    } else {
      return new ImagesInGallery(
        images,
        image -> this.fileStorage.retrieve(image.getId(), "png")
      );
    }
  }

  @Override
  public Gallery edit(Gallery gallery) {
    return galleryDao.edit(gallery);
  }
}
