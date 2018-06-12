package org.sing_group.piba.rest.entity.video;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "video-source", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "video-data", description = "Information of a video.")
public class VideoSource {
  
  @XmlElement(name="type")
  private String mimeType;
  
  @XmlElement(name="src")
  private String url;

  public VideoSource(String mimeType, String url) {
    super();
    this.mimeType = mimeType;
    this.url = url;
  }
  
  public String getMimeType() {
    return mimeType;
  }
  
  public String getUrl() {
    return url;
  }
}
