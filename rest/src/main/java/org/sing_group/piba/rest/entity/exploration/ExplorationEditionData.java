package org.sing_group.piba.rest.entity.exploration;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "exploration-edition-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "exploration-edition-data", description = "Information of a exploration for edition.")
public class ExplorationEditionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id", required = true)
  private String id;

  @XmlElement(name = "location", required = true)
  private String location;

  @XmlElement(name = "date", required = true)
  private Date date;
  
  @XmlElement(name="patient")
  private String patient;


  public ExplorationEditionData() {}

  public ExplorationEditionData(String id, String location, Date date) {
    this.id = id;
    this.location = location;
    this.date = date;
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

  public String getPatient() {
    return patient;
  }

  public void setPatient(String patient) {
    this.patient = patient;
  }
   
}
