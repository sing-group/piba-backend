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
package org.sing_group.piba.domain.entities.exploration;

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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;

import org.sing_group.piba.domain.entities.Identifiable;
import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.video.Video;

@Entity
@Table(name = "exploration")
public class Exploration implements Identifiable {
  @Id
  private String id;

  @Column(name = "title", nullable = false, unique = true)
  private String title;

  @Column(name = "location", nullable = false)
  private String location;

  @Column(name = "date", nullable = false)
  private Timestamp date;

  @Column(name = "creation_date", columnDefinition = "DATETIME(3)")
  private Timestamp creationDate;

  @Version
  @Column(name = "update_date", columnDefinition = "DATETIME(3)")
  private Timestamp updateDate;

  @Column(name = "confirmed", columnDefinition="BIT(1) DEFAULT FALSE")
  private boolean confirmed;

  @OrderBy("title ASC")
  @OneToMany(mappedBy = "exploration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Video> videos = new ArrayList<>();

  @OrderBy("name ASC")
  @OneToMany(mappedBy = "exploration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Polyp> polyps = new ArrayList<>();

  @ManyToOne
  private Patient patient;

  Exploration() {}

  public Exploration(String title, String location, Date date, Patient patient, boolean confirmed) {
    id = UUID.randomUUID().toString();
    this.setTitle(title);
    this.setLocation(location);
    this.setDate(date);
    this.setPatient(patient);
    this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
    this.confirmed = confirmed;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    checkArgument(title, t -> requireNonNull(t, "title cannot be null"));
    this.title = title;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    checkArgument(location, l -> requireNonNull(l, "location cannot be null"));
    this.location = location;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    checkArgument(date, d -> requireNonNull(d, "exploration date cannot be null"));
    this.date = new Timestamp(date.getTime());
  }

  public void addVideo(Video video) {
    video.setExploration(this);
  }

  public void internalRemoveVideo(Video video) {
    this.videos.remove(video);
  }

  public void internalAddVideo(Video video) {
    this.videos.add(video);
  }

  public List<Video> getVideos() {
    return videos;
  }

  public void addPolyp(Polyp polyp) {
    polyp.setExploration(this);
  }

  public void internalRemovePolyp(Polyp polyp) {
    this.polyps.remove(polyp);
  }

  public void internalAddPolyp(Polyp polyp) {
    this.polyps.add(polyp);
  }

  public List<Polyp> getPolyps() {
    return polyps;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    if (this.patient != null) {
      this.patient.internalRemoveExploration(this);
    }
    this.patient = patient;
    if (patient != null) {
      this.patient.internalAddExploration(this);
    }
  }

  public int getNumPolyps() {
    return polyps.size();
  }

  public int getNumVideos() {
    return videos.size();
  }

  public boolean isConfirmed() {
    return this.confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

}
