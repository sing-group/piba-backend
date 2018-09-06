package org.sing_group.piba.rest.entity.videomodification;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.rest.entity.UuidAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "videomodification-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
  value = "videomodification-data", description = "Information the relationship between videos and modifiers for edition."
)
public class VideoModificationData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id")
  private int id;

  @XmlElement(name = "video")
  private UuidAndUri video;

  @XmlElement(name = "modifier")
  private UuidAndUri modifier;

  @XmlElement(name = "start")
  private Integer start;

  @XmlElement(name = "end")
  private Integer end;

  VideoModificationData() {}

  public VideoModificationData(int id, UuidAndUri video, UuidAndUri modifier, Integer start, Integer end) {
    super();
    this.id = id;
    this.video = video;
    this.modifier = modifier;
    this.start = start;
    this.end = end;
  }

  public int getId() {
    return id;
  }

  public UuidAndUri getVideo() {
    return video;
  }

  public UuidAndUri getModifier() {
    return modifier;
  }

  public Integer getStart() {
    return start;
  }

  public Integer getEnd() {
    return end;
  }

}
