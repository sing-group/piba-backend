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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.sing_group.piba.domain.dao.spi.image.GalleryDAO;
import org.sing_group.piba.domain.entities.image.Gallery;
import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.PolypLocation;
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
  public StreamingOutput getGalleriesInZip(Gallery gallery, String filter, Boolean withLocation)
    throws FileNotFoundException {
    Stream<Image> images = imageService.listImagesBy(gallery, null, null, filter);

    StreamingOutput sout = new StreamingOutput() {
      @Override
      public void write(OutputStream out) throws IOException, WebApplicationException {
        ZipOutputStream zout = new ZipOutputStream(out);
        images.forEach(image -> {
          InputStream imageStream = fileStorage.retrieve(image.getId(), "png");

          try {
            BufferedImage imageData = ImageIO.read(imageStream);
            if (withLocation) {
              PolypLocation polypLocation = null;
              try {
                polypLocation =
                  imageService.getPolypLocation(imageService.get(image.getId()));
              } catch (EJBException e) {
                if (e.getCause() instanceof IllegalArgumentException) {
                  // catched "No location for image:", which is normal when an
                  // image does not have an assigned polyp location
                } else {
                  throw e;
                }
              }
              // draw the polyp location
              if (polypLocation != null) {
                Graphics2D g2 = imageData.createGraphics();
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(
                  polypLocation.getX(), polypLocation.getY(),
                  polypLocation.getWidth(), polypLocation.getHeight()
                );
              }
            }

            String polypId = image.getPolyp() == null ? "" : image.getPolyp().getId() + "_";
            ZipEntry zipEntry =
              new ZipEntry(
                polypId + image.getVideo().getId() + "_" + image.getNumFrame() + ".png"
              );
            zout.putNextEntry(zipEntry);
            ImageIO.write(imageData, "png", zout);

          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
        zout.flush();
        zout.close();
      }
    };
    return sout;
  }

  @Override
  public Gallery edit(Gallery gallery) {
    return galleryDao.edit(gallery);
  }
}
