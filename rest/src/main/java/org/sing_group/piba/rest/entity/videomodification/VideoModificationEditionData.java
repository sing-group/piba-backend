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
package org.sing_group.piba.rest.entity.videomodification;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "videomodification-edition-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
  value = "videomodification-edition-data", description = "Information the relationship between videos and modifiers for edition."
)
public class VideoModificationEditionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id")
  private int id;

  @XmlElement(name = "video")
  private String video;

  @XmlElement(name = "modifier")
  private String modifier;

  @XmlElement(name = "start")
  private Integer start;

  @XmlElement(name = "end")
  private Integer end;
  
  @XmlElement(name = "confirmed")
  private boolean confirmed;

  VideoModificationEditionData() {}

  public VideoModificationEditionData(int id, String video, String modifier, Integer start, Integer end, boolean confirmed) {
    super();
    this.id = id;
    this.video = video;
    this.modifier = modifier;
    this.start = start;
    this.end = end;
    this.confirmed = confirmed;
  }

  public int getId() {
    return id;
  }

  public String getVideo() {
    return video;
  }

  public String getModifier() {
    return modifier;
  }

  public Integer getStart() {
    return start;
  }

  public Integer getEnd() {
    return end;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }
  
}
