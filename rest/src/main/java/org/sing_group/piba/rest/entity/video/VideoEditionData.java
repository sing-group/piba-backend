package org.sing_group.piba.rest.entity.video;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import io.swagger.annotations.ApiModelProperty;

public class VideoEditionData implements Serializable {
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

  @XmlElement(name = "video_sources")
  private List<VideoSource> urls;

  @XmlElement(name = "exploration")
  private String exploration;

  public VideoEditionData() {}

  public VideoEditionData(String id, String title, String observations, List<VideoSource> urls, boolean isProcessing, String exploration) {
    this.id = id;
    this.title = title;
    this.observations = observations;
    this.urls = urls;
    this.isProcessing = isProcessing;
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

  public String getExploration() {
    return exploration;
  }
  
}