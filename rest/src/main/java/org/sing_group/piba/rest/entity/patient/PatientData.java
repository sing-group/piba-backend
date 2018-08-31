package org.sing_group.piba.rest.entity.patient;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.domain.entities.patient.Patient.SEX;
import org.sing_group.piba.rest.entity.UuidAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "patient-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "patient-data", description = "Information of a patient.")
public class PatientData {

  @XmlElement(name = "id", required = true)
  private String id;
  @XmlElement(name = "patientID", required = true)
  private String patientID;
  @XmlElement(name = "sex")
  private SEX sex;
  @XmlElement(name = "birthdate")
  private Date birthdate;
  @XmlElement(name = "explorations")
  private List<UuidAndUri> explorations;
  @XmlElement(name="idSpace")
  private UuidAndUri idSpace;

  public PatientData() {}

  public PatientData(String id, String patientID, SEX sex, Date birthdate, List<UuidAndUri> explorations, UuidAndUri idSpace) {
    this.id = id;
    this.patientID = patientID;
    this.sex = sex;
    this.birthdate = birthdate;
    this.explorations = explorations;
    this.idSpace = idSpace;
  }

  public String getId() {
    return id;
  }

  public String getPatientID() {
    return patientID;
  }

  public SEX getSex() {
    return sex;
  }

  public Date getBirthdate() {
    return birthdate;
  }

  public List<UuidAndUri> getExplorations() {
    return explorations;
  }

  public UuidAndUri getIdSpace() {
    return idSpace;
  }

}
