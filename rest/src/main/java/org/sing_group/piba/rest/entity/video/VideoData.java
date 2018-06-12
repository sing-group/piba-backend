/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Florentino Fdez-Riverola, Alba Nogueira Rodríguez
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


package org.sing_group.piba.rest.entity.video;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "video-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "video-data", description = "Information of a video.")
public class VideoData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id", required = true)
  private String id;
  
  @XmlElement(name = "title")
  private String title;
  
  @XmlElement(name = "observations")
  private String observations;
  
  @XmlElement(name = "video_sources")
  private List<VideoSource> urls;
  
  VideoData() {}

  public VideoData(String id, String title, String observations, List<VideoSource> urls) {
    this.id = id;
    this.title = title;
    this.observations = observations;
    this.urls = urls;
  }
  
  public String getId() {
    return id;
  }
  
  public String getObservations() {
    return observations;
  }
  
  public String getTitle() {
    return title;
  }
  
  public List<VideoSource> getUrls() {
    return urls;
  }
}
