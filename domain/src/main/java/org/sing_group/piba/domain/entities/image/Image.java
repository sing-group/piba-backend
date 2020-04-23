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
package org.sing_group.piba.domain.entities.image;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.sing_group.piba.domain.entities.Identifiable;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.video.Video;

@Entity
@Table(name = "image", uniqueConstraints = @UniqueConstraint(columnNames = {
  "gallery_id", "video_id", "num_frame"
}))
public class Image implements Identifiable {

  @Id
  private String id;

  @Column(name = "num_frame")
  private int numFrame;

  @Column(name = "is_removed")
  private boolean isRemoved;

  @Column(name = "creation_date", columnDefinition = "DATETIME(3)")
  private Timestamp creationDate;

  @Version
  @Column(name = "update_date", columnDefinition = "DATETIME(3)")
  private Timestamp updateDate;

  @Column(name = "observation_to_remove")
  private String observationToRemove;

  @Column(name = "observation")
  private String observation;

  @Column(name = "manually_selected", columnDefinition = "BIT(1) DEFAULT FALSE")
  private boolean manuallySelected;

  @ManyToOne
  private Gallery gallery;

  @ManyToOne
  private Video video;

  @ManyToOne
  private Polyp polyp;

  @OneToOne(mappedBy = "image", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
  private PolypLocation polypLocation;

  Image() {}

  public Image(
    int numFrame, boolean isRemoved, String observation, boolean manuallySelected, Gallery gallery, Video video,
    Polyp polyp
  ) {
    this.id = UUID.randomUUID().toString();
    this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
    this.numFrame = numFrame;
    this.isRemoved = isRemoved;
    this.observation = observation;
    this.manuallySelected = manuallySelected;
    this.setGallery(gallery);
    this.setVideo(video);
    this.setPolyp(polyp);
  }

  @Override
  public String getId() {
    return id;
  }

  public int getNumFrame() {
    return numFrame;
  }

  public void setNumFrame(int numFrame) {
    this.numFrame = numFrame;
  }

  public boolean isRemoved() {
    return isRemoved;
  }

  public void setRemoved(boolean isRemoved) {
    this.isRemoved = isRemoved;
  }

  public String getObservationToRemove() {
    return observationToRemove;
  }

  public void setObservationToRemove(String observationToRemove) {
    this.observationToRemove = observationToRemove;
  }

  public String getObservation() {
    return observation;
  }

  public void setObservation(String observation) {
    this.observation = observation;
  }

  public Gallery getGallery() {
    return gallery;
  }

  public void setGallery(Gallery gallery) {
    this.gallery = gallery;
  }

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

  public Polyp getPolyp() {
    return polyp;
  }

  public void setPolyp(Polyp polyp) {
    this.polyp = polyp;
  }

  public PolypLocation getPolypLocation() {
    return polypLocation;
  }

  public void setPolypLocation(PolypLocation polypLocation) {
    this.polypLocation = polypLocation;
  }

  public boolean isManuallySelected() {
    return manuallySelected;
  }

  public void setManuallySelected(boolean manuallySelected) {
    this.manuallySelected = manuallySelected;
  }
}
