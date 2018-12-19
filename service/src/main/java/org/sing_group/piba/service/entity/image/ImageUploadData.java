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
package org.sing_group.piba.service.entity.image;

import java.io.File;

public class ImageUploadData {

  private int numFrame;
  private File imageData;
  private String gallery, video;

  public ImageUploadData(int numFrame, File imageData, String gallery, String video) {
    this.numFrame = numFrame;
    this.imageData = imageData;
    this.gallery = gallery;
    this.video = video;
  }

  public int getNumFrame() {
    return numFrame;
  }

  public File getImageData() {
    return imageData;
  }

  public String getGallery() {
    return gallery;
  }

  public String getVideo() {
    return video;
  }

}
