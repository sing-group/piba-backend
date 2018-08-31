package org.sing_group.piba.domain.entities.patient;

import static java.util.Objects.requireNonNull;

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
  private Date birthdate;

  @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Exploration> explorations = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "idSpace_id")
  private IdSpace idSpace;

  Patient() {}

  public Patient(String patientID, SEX sex, Date birthdate, IdSpace idSpace) {
    this.id = UUID.randomUUID().toString();
    setPatientID(patientID);
    this.sex = sex;
    this.birthdate = birthdate;
    setIdSpace(idSpace);
  }

  @Override
  public String getId() {
    return id;
  }

  public String getPatientID() {
    return patientID;
  }

  public void setPatientID(String publicID) {
    requireNonNull(publicID);
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
    this.birthdate = birthdate;
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
    requireNonNull(idSpace.getId());
    this.idSpace = idSpace;
  }

}
