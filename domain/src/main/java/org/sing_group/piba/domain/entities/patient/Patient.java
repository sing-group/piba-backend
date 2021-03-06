/*-
 * #%L
 * Domain
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
package org.sing_group.piba.domain.entities.patient;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.checkArgument;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.sing_group.piba.domain.entities.Identifiable;
import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.idspace.IdSpace;

@Entity
@Table(name = "patient", uniqueConstraints = @UniqueConstraint(columnNames = {
  "patientID", "idSpace_id"
}))
public class Patient implements Identifiable {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "patientID", nullable = false)
  private String patientID;

  @Enumerated(EnumType.STRING)
  @Column(name = "sex")
  private SEX sex;

  public enum SEX {
    MALE, FEMALE
  }

  @Column(name = "birthdate")
  private Timestamp birthdate;

  @Column(name = "creation_date", columnDefinition = "DATETIME(3)")
  private Timestamp creationDate;

  @Version
  @Column(name = "update_date", columnDefinition = "DATETIME(3)")
  private Timestamp updateDate;

  @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Exploration> explorations = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "idSpace_id")
  private IdSpace idSpace;

  Patient() {}

  public Patient(String patientID, SEX sex, Date birthdate, IdSpace idSpace) {
    this.id = UUID.randomUUID().toString();
    this.sex = sex;
    this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
    this.setPatientID(patientID);
    this.setBirthdate(birthdate);
    this.setIdSpace(idSpace);
  }

  @Override
  public String getId() {
    return id;
  }

  public String getPatientID() {
    return patientID;
  }

  public void setPatientID(String publicID) {
    checkArgument(publicID, p -> requireNonNull(p, "patient identifier cannot be null"));
    this.patientID = publicID;
  }

  public SEX getSex() {
    return sex;
  }

  public void setSex(SEX sex) {
    this.sex = sex;
  }

  public Date getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(Date birthdate) {
    this.birthdate = new Timestamp(birthdate.getTime());
  }

  public void internalRemoveExploration(Exploration exploration) {
    this.explorations.remove(exploration);
  }

  public void internalAddExploration(Exploration exploration) {
    this.explorations.add(exploration);
  }

  public List<Exploration> getExplorations() {
    return explorations;
  }

  public void addExploration(Exploration exploration) {
    exploration.setPatient(this);
  }

  public void removeExploration(Exploration exploration) {
    exploration.setPatient(null);
  }

  public IdSpace getIdSpace() {
    return idSpace;
  }

  public void setIdSpace(IdSpace idSpace) {
    checkArgument(idSpace, i -> requireNonNull(i, "IdSpace cannot be null"));
    this.idSpace = idSpace;
  }

}
