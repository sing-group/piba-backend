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
package org.sing_group.piba.rest.entity.image;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.rest.entity.UuidAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "image-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "image-data", description = "Information of a image.")
public class ImageData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id")
  private String id;
  @XmlElement(name = "numFrame")
  private int numFrame;
  @XmlElement(name = "isRemoved")
  private boolean isRemoved;
  @XmlElement(name = "gallery")
  private UuidAndUri gallery;
  @XmlElement(name = "video")
  private UuidAndUri video;

  public ImageData() {}

  public ImageData(String id, int numFrame, boolean isRemoved, UuidAndUri gallery, UuidAndUri video) {
    this.id = id;
    this.numFrame = numFrame;
    this.isRemoved = isRemoved;
    this.gallery = gallery;
    this.video = video;
  }

  public String getId() {
    return id;
  }

  public Integer getNumFrame() {
    return numFrame;
  }

  public boolean isRemoved() {
    return isRemoved;
  }

  public UuidAndUri getGallery() {
    return gallery;
  }

  public UuidAndUri getVideo() {
    return video;
  }

}
