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
package org.sing_group.piba.domain.entities.polyprecording;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.sing_group.piba.domain.entities.VideoInterval;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyp.ReviewedPolypRecording;
import org.sing_group.piba.domain.entities.video.Video;

@Entity
@Table(name = "polyprecording", uniqueConstraints = @UniqueConstraint(columnNames = {
  "video_id", "polyp_id", "start", "end"
}))
public class PolypRecording extends VideoInterval implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;

  @Column(name = "creation_date", columnDefinition = "DATETIME(3)")
  private Timestamp creationDate;

  @Version
  @Column(name = "update_date", columnDefinition = "DATETIME(3)")
  private Timestamp updateDate;

  // Needed for ReviewedPolypRecording relation
  @Column(name = "polyp_id", insertable = false, updatable = false)
  private String polypId;
  
  @ManyToOne
  @JoinColumn(name = "polyp_id", referencedColumnName = "id")
  private Polyp polyp;

  @ManyToOne
  private Video video;

  @Column(name = "confirmed", columnDefinition = "BIT(1) DEFAULT FALSE")
  private boolean confirmed;
  
  @OneToMany(mappedBy = "polypRecording", fetch = FetchType.LAZY)
  private Set<ReviewedPolypRecording> reviewedPolypRecordings;

  PolypRecording() {}

  public PolypRecording(Polyp polyp, Video video, Integer start, Integer end, boolean confirmed) {
    this.polyp = polyp;
    this.polypId = this.polyp.getId();
    this.video = video;
    this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
    this.confirmed = confirmed;
    this.setStart(start);
    this.setEnd(end);
    this.checkInterval(start, end);
  }

  public PolypRecording(int id, Polyp polyp, Video video, Integer start, Integer end, boolean confirmed) {
    this.id = id;
    this.polyp = polyp;
    this.polypId = this.polyp.getId();
    this.video = video;
    this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
    this.confirmed = confirmed;
    this.setStart(start);
    this.setEnd(end);
    this.checkInterval(start, end);
  }

  public int getId() {
    return id;
  }

  public Polyp getPolyp() {
    return polyp;
  }

  public void setPolyp(Polyp polyp) {
    this.polyp = polyp;
  }

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }
  
  public boolean isReviewedInDataset(String datasetId) {
    return this.reviewedPolypRecordings.stream()
      .anyMatch(rpr -> rpr.getPolypDataset().getId().equals(datasetId));
  }

  @Override
  public String toString() {
    return "PolypRecording [id=" + id + ", creationDate=" + creationDate + ", updateDate=" + updateDate + ", polyp="
      + polyp + ", video=" + video + ", confirmed=" + confirmed + "]";
  }
}
