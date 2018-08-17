package org.sing_group.piba.rest.entity.polyptrecording;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "polyprecording-edition-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
  value = "polyprecording-edition-data", description = "Information the relationship between polyps and videos for edition."
)
public class PolypRecordingEditicionData implements Serializable {

  private static final long serialVersionUID = 1L;

  @XmlElement(name = "video")
  private String video;

  @XmlElement(name = "polyp")
  private String polyp;

  @XmlElement(name = "start")
  private Integer start;

  @XmlElement(name = "end")
  private Integer end;

  PolypRecordingEditicionData() {}

  public PolypRecordingEditicionData(String video, String polyp, Integer start, Integer end) {
    super();
    this.video = video;
    this.polyp = polyp;
    this.start = start;
    this.end = end;
  }

  public String getVideo() {
    return video;
  }

  public String getPolyp() {
    return polyp;
  }

  public Integer getStart() {
    return start;
  }

  public Integer getEnd() {
    return end;
  }
}
