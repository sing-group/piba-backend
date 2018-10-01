/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato,
 * 			Florentino Fdez-Riverola, Alba Nogueira Rodríguez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
