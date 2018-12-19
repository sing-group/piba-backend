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

import java.io.File;

import javax.ws.rs.ext.Provider;

import org.sing_group.piba.rest.MultipartMessageBodyReader;
import org.sing_group.piba.rest.entity.RestImageUploadData;

@Provider
public class RestImageUploadDataReader extends MultipartMessageBodyReader<RestImageUploadData> {

  private int numFrame;
  private File imageData;
  private String gallery, video;

  @Override
  protected void init() {}

  @Override
  protected void add(String name, String value) {
    switch (name) {
      case "numFrame":
        this.numFrame = Integer.valueOf(value);
        break;
      case "gallery":
        this.gallery = value;
        break;
      case "video":
        this.video = value;
        break;
      default:
        break;
    }
  }

  @Override
  protected void add(String name, File uploadedFile) {
    switch (name) {
      case "image":
        this.imageData = uploadedFile;
        break;
    }
  }

  @Override
  protected RestImageUploadData build() {
    return new RestImageUploadData(this.numFrame, this.imageData, this.gallery, this.video);
  }

}
