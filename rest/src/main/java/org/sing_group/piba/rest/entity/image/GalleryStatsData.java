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

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "gallery-stats-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "gallery-stats-data", description = "Statistical information of a gallery.")
public class GalleryStatsData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "countImages", required = true)
  private int countImages;
  
  @XmlElement(name = "countImagesWithoutPolyp", required = true)
  private int countImagesWithoutPolyp;

  @XmlElement(name = "countImagesWithPolyp", required = true)
  private int countImagesWithPolyp;

  @XmlElement(name = "countImagesWithoutLocation", required = true)
  private int countImagesWithoutLocation;

  @XmlElement(name = "countImagesWithLocation", required = true)
  private int countImagesWithLocation;
  
  @XmlElement(name = "countImagesWithoutPolypAndLocation", required = true)
  private int countImagesWithoutPolypAndLocation;
  
  @XmlElement(name = "countImagesWithoutPolypAndWithLocation", required = true)
  private int countImagesWithoutPolypAndWithLocation;

  @XmlElement(name = "countImagesWithPolypAndWithoutLocation", required = true)
  private int countImagesWithPolypAndWithoutLocation;

  @XmlElement(name = "countImagesWithPolypAndLocation", required = true)
  private int countImagesWithPolypAndLocation;

  GalleryStatsData() {}

  public GalleryStatsData(
    int countImages, int countImagesWithoutPolyp, int countImagesWithPolyp, int countImagesWithoutLocation,
    int countImagesWithLocation, int countImagesWithoutPolypAndLocation, int countImagesWithoutPolypAndWithLocation,
    int countImagesWithPolypAndWithoutLocation, int countImagesWithPolypAndLocation
  ) {
    super();
    this.countImages = countImages;
    this.countImagesWithoutPolyp = countImagesWithoutPolyp;
    this.countImagesWithPolyp = countImagesWithPolyp;
    this.countImagesWithoutLocation = countImagesWithoutLocation;
    this.countImagesWithLocation = countImagesWithLocation;
    this.countImagesWithoutPolypAndLocation = countImagesWithoutPolypAndLocation;
    this.countImagesWithoutPolypAndWithLocation = countImagesWithoutPolypAndWithLocation;
    this.countImagesWithPolypAndWithoutLocation = countImagesWithPolypAndWithoutLocation;
    this.countImagesWithPolypAndLocation = countImagesWithPolypAndLocation;
  }



  public int getCountImages() {
    return countImages;
  }

  public int getCountImagesWithoutPolyp() {
    return countImagesWithoutPolyp;
  }

  public int getCountImagesWithPolyp() {
    return countImagesWithPolyp;
  }

  public int getCountImagesWithoutLocation() {
    return countImagesWithoutLocation;
  }

  public int getCountImagesWithLocation() {
    return countImagesWithLocation;
  }

  public int getCountImagesWithoutPolypAndLocation() {
    return countImagesWithoutPolypAndLocation;
  }

  public int getCountImagesWithoutPolypAndWithLocation() {
    return countImagesWithoutPolypAndWithLocation;
  }

  public int getCountImagesWithPolypAndWithoutLocation() {
    return countImagesWithPolypAndWithoutLocation;
  }

  public int getCountImagesWithPolypAndLocation() {
    return countImagesWithPolypAndLocation;
  }
  
}
