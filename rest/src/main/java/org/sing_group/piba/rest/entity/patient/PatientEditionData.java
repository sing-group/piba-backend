package org.sing_group.piba.rest.entity.patient;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.domain.entities.patient.Patient.SEX;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "patient-edition-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "patient-edition-data", description = "Information of a patient for edition.")
public class PatientEditionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id", required = true)
  private String id;
  @XmlElement(name = "patientID", required = true)
  private String patientID;
  @XmlElement(name = "sex")
  private SEX sex;
  @XmlElement(name = "birthdate")
  private Date birthdate;
  @XmlElement(name = "idSpace")
  private String idSpace;

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

  public String getIdSpace() {
    return idSpace;
  }

}
