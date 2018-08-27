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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.exploration.Exploration;

@Entity
@Table(name = "patient")
public class Patient {

  @Id
  @Column(name = "id")
  private String id;
  @Column(name = "publicID", nullable = false)
  private String publicID;
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

  Patient() {}

  public Patient(String publicID, SEX sex, Date birthdate) {
    this.id = UUID.randomUUID().toString();
    setPublicID(publicID);
    this.sex = sex;
    this.birthdate = birthdate;
  }

  public String getId() {
    return id;
  }

  public String getPublicID() {
    return publicID;
  }

  public void setPublicID(String publicID) {
    requireNonNull(publicID);
    this.publicID = publicID;
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

}
