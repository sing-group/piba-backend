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

  @XmlElement(name = "video")
  private UuidAndUri video;

  @XmlElement(name = "polyp")
  private UuidAndUri polyp;

  @XmlElement(name = "start")
  private Integer start;

  @XmlElement(name = "end")
  private Integer end;

  PolypRecordingData() {}

  public PolypRecordingData(UuidAndUri video, UuidAndUri polyp, Integer start, Integer end) {
    super();
    this.video = video;
    this.polyp = polyp;
    this.start = start;
    this.end = end;
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
