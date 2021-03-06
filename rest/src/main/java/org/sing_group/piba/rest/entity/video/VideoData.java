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

import org.sing_group.piba.rest.entity.UuidAndUri;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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

  @XmlElement(name = "processing")
  @ApiModelProperty(name = "processing")
  private boolean isProcessing;

  @XmlElement(name = "withText")
  @ApiModelProperty(name = "withText")
  private boolean withText;

  @XmlElement(name = "fps")
  private int fps;

  @XmlElement(name = "video_sources")
  private List<VideoSource> urls;

  @XmlElement(name = "exploration")
  private UuidAndUri exploration;

  public VideoData() {}

  public VideoData(
    String id, String title, String observations, List<VideoSource> urls, boolean isProcessing,
    boolean withText, int fps, UuidAndUri exploration
  ) {
    this.id = id;
    this.title = title;
    this.observations = observations;
    this.urls = urls;
    this.isProcessing = isProcessing;
    this.withText = withText;
    this.fps = fps;
    this.exploration = exploration;
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

  public boolean isProcessing() {
    return isProcessing;
  }

  public boolean isWithText() {
    return withText;
  }

  public int getFps() {
    return fps;
  }

  public UuidAndUri getExploration() {
    return exploration;
  }

}
