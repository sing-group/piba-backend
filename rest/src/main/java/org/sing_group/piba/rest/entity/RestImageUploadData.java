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
package org.sing_group.piba.rest.entity;

import java.io.File;
import java.io.Serializable;

import org.sing_group.piba.service.entity.image.ImageUploadData;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "image-upload-data", description = "Upload data of a image.")
public class RestImageUploadData extends ImageUploadData implements Serializable {
  private static final long serialVersionUID = 1L;

  public RestImageUploadData(
    File imageData, int numFrame, String gallery, String video, String polyp, String observation,
    boolean manuallySelected
  ) {
    super(imageData, numFrame, gallery, video, polyp, observation, manuallySelected);
  }

}
