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
package org.sing_group.piba.rest.entity.polyprecording;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.rest.entity.UuidAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "polyprecording-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "polyprecording-data", description = "Information the relationship between polyps and videos.")

public class PolypRecordingData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id")
  private int id;
  
  @XmlElement(name = "video")
  private UuidAndUri video;

  @XmlElement(name = "polyp")
  private UuidAndUri polyp;

  @XmlElement(name = "start")
  private Integer start;

  @XmlElement(name = "end")
  private Integer end;

  PolypRecordingData() {}

  public PolypRecordingData(int id, UuidAndUri video, UuidAndUri polyp, Integer start, Integer end) {
    super();
    this.id = id;
    this.video = video;
    this.polyp = polyp;
    this.start = start;
    this.end = end;
  }

  public int getId() {
    return id;
  } 
  
  public UuidAndUri getVideo() {
    return video;
  }

  public UuidAndUri getPolyp() {
    return polyp;
  }

  public Integer getStart() {
    return start;
  }

  public Integer getEnd() {
    return end;
  }

}
