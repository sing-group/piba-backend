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

import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.image.ImageDAO;
import org.sing_group.piba.domain.entities.image.Gallery;
import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.ImageFilter;
import org.sing_group.piba.domain.entities.image.PolypLocation;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.service.entity.image.ImageUploadData;
import org.sing_group.piba.service.spi.image.GalleryService;
import org.sing_group.piba.service.spi.image.ImageService;
import org.sing_group.piba.service.spi.polyp.PolypService;
import org.sing_group.piba.service.spi.storage.FileStorage;
import org.sing_group.piba.service.spi.video.VideoService;

@Stateless
@PermitAll
public class DefaultImageService implements ImageService {

  @Inject
  private ImageDAO imageDao;

  @Inject
  private FileStorage fileStorage;

  @Inject
  private GalleryService galleryService;

  @Inject
  private VideoService videoService;

  @Inject
  private PolypService polypService;

  @Override
  public Image create(ImageUploadData imageUploadData) {
    Gallery gallery = this.galleryService.get(imageUploadData.getGallery());
    Video video = this.videoService.getVideo(imageUploadData.getVideo());
    Polyp polyp = null;
    if (imageUploadData.getPolyp() != null) {
      polyp = this.polypService.getPolyp(imageUploadData.getPolyp());
    }
    Image image =
      new Image(
        imageUploadData.getNumFrame(), false, imageUploadData.getObservation(), imageUploadData.isManuallySelected(),
        gallery, video, polyp
      );
    image = this.imageDao.create(image);
    try {
      fileStorage.store(
        image.getId(),
        Files.probeContentType(imageUploadData.getImageData().toPath()).replaceAll("[^/]+/", ""),
        new FileInputStream(imageUploadData.getImageData())
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return image;
  }

  @Override
  public boolean existsImage(String id) {
    return imageDao.existsImage(id);
  }

  @Override
  public Image get(String id) {
    return imageDao.get(id);
  }

  @Override
  public PolypLocation createPolypLocation(PolypLocation polypLocation) {
    return imageDao.createPolypLocation(polypLocation);
  }

  @Override
  public PolypLocation modifyPolypLocation(PolypLocation polypLocation) {
    return imageDao.modifyPolypLocation(polypLocation);
  }
  
  @Override
  public PolypLocation getPolypLocation(Image image) {
    return imageDao.getPolypLocation(image);
  }
  
  @Override
  public Optional<PolypLocation> getPolypLocationIfPresent(Image image) {
    return imageDao.getPolypLocationIfPresent(image);
  }

  @Override
  public void delete(Image image) {
    imageDao.delete(image);
  }

  @Override
  public void deletePolypLocation(Image image) {
    imageDao.deletePolypLocation(image);
  }

  @Override
  public Stream<Image> listImagesByGallery(Gallery gallery, Integer page, Integer pageSize, ImageFilter filter) {
    return imageDao.listImagesByGallery(gallery, page, pageSize, filter);
  }

  @Override
  public Stream<String> listImagesIdentifiersByGallery(Gallery gallery, Integer page, Integer pageSize, ImageFilter filter) {
    return imageDao.listImagesIdentifiersByGallery(gallery, page, pageSize, filter);
  }

  @Override
  public Stream<String> listImageObservationsToRemoveBy(String observationToRemoveStartsWith) {
    return imageDao.listImageObservationsToRemoveBy(observationToRemoveStartsWith);
  }

  @Override
  public int countImagesByGallery(Gallery gallery, ImageFilter filter) {
    return imageDao.countImagesInGallery(gallery, filter);
  }
  
  @Override
  public Stream<Image> listImagesByPolyp(Polyp polyp, Integer page, Integer pageSize, ImageFilter filter) {
    return this.imageDao.listImagesByPolyp(polyp, page, pageSize, filter);
  }
  
  @Override
  public int countImagesByPolyp(Polyp polyp, ImageFilter filter) {
    return this.imageDao.countImagesInPolyp(polyp, filter);
  }
  
  @Override
  public Stream<Image> listImagesByPolypAndGallery(
    Polyp polyp, Gallery gallery, Integer page, Integer pageSize, ImageFilter filter
  ) {
    return this.imageDao.listImagesByPolypAndGallery(polyp, gallery, page, pageSize, filter);
  }
  
  @Override
  public int countImagesByPolypAndGallery(Polyp polyp, Gallery gallery, ImageFilter filter) {
    return this.imageDao.countImagesByPolypAndGallery(polyp, gallery, filter);
  }

}
