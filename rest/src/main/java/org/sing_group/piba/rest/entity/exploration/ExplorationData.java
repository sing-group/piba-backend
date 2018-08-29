package org.sing_group.piba.rest.entity.exploration;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.rest.entity.UuidAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "exploration-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "exploration-data", description = "Information of a exploration.")
public class ExplorationData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id", required = true)
  private String id;

  @XmlElement(name = "location", required = true)
  private String location;

  @XmlElement(name = "date", required = true)
  private Date date;

  @XmlElement(name = "videos")
  private List<UuidAndUri> videos;

  @XmlElement(name = "polyps")
  private List<UuidAndUri> polyps;

  @XmlElement(name = "patient")
  private UuidAndUri patient;

  public ExplorationData() {}

  public ExplorationData(
    String id, String location, Date date, List<UuidAndUri> videos, List<UuidAndUri> polyps, UuidAndUri patient
  ) {
    this.id = id;
    this.location = location;
    this.date = date;
    this.videos = videos;
    this.polyps = polyps;
    this.patient = patient;
  }

  public String getId() {
    return id;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public List<UuidAndUri> getVideos() {
    return videos;
  }

  public List<UuidAndUri> getPolyps() {
    return polyps;
  }

  public UuidAndUri getPatient() {
    return patient;
  }

}
