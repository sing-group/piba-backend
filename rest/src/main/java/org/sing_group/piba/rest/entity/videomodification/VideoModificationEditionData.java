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

  @XmlElement(name = "video")
  private String video;

  @XmlElement(name = "modifier")
  private String modifier;

  @XmlElement(name = "start")
  private Integer start;

  @XmlElement(name = "end")
  private Integer end;

  VideoModificationEditionData() {}

  public VideoModificationEditionData(String video, String modifier, Integer start, Integer end) {
    super();
    this.video = video;
    this.modifier = modifier;
    this.start = start;
    this.end = end;
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

}
