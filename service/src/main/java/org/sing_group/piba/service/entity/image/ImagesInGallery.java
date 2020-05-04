/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato,
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
package org.sing_group.piba.service.entity.image;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.PolypLocation;

public class ImagesInGallery {
  private final Stream<Image> images;

  private final Function<Image, InputStream> inputStreamProvider;
  private final Function<Image, Optional<PolypLocation>> polypLocationProvider;

  public ImagesInGallery(
    Stream<Image> images,
    Function<Image, InputStream> inputStreamProvider
  ) {
    this(images, inputStreamProvider, image -> Optional.empty());
  }

  public ImagesInGallery(
    Stream<Image> images,
    Function<Image, InputStream> inputStreamProvider,
    Function<Image, Optional<PolypLocation>> polypLocationProvider
  ) {
    this.images = images;
    this.inputStreamProvider = inputStreamProvider;
    this.polypLocationProvider = polypLocationProvider;
  }

  public void writeTo(OutputStream out) throws IOException {
    try (ZipOutputStream zout = new ZipOutputStream(out)) {
      this.images.forEach(image -> {
        try (final InputStream imageStream = this.inputStreamProvider.apply(image)) {
          final BufferedImage imageData = ImageIO.read(imageStream);
          
          final Optional<PolypLocation> polypLocation = this.polypLocationProvider.apply(image);
          
          polypLocation.ifPresent(location -> {
            final Graphics2D g2 = imageData.createGraphics();
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(
              location.getX(), location.getY(),
              location.getWidth(), location.getHeight()
            );
          });
          
          final String imageName = String.format("%s%s_%d.png",
            image.getPolyp() == null ? "" : image.getPolyp().getId() + "_",
            image.getVideo().getId(),
            image.getNumFrame()
          );
          
          zout.putNextEntry(new ZipEntry(imageName));
          ImageIO.write(imageData, "png", zout);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    } catch (RuntimeException re) {
      re.printStackTrace();
      if (re.getCause() instanceof IOException) {
        throw (IOException) re.getCause();
      } else {
        throw re;
      }
    }
  }

}
