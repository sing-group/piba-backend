/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2018 - 2021 Daniel Glez-Peña, Miguel Reboiro-Jato,
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
package org.sing_group.piba.domain.entities.polyp;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;

@Entity
@Table(name = "reviewedpolyprecording")
@IdClass(ReviewedPolypRecordingId.class)
public class ReviewedPolypRecording {

  @Id
  @Column(name = "polypdataset_id")
  private String polypDatasetId;

  @Id
  @Column(name = "polyp_id")
  private String polypId;

  @Id
  @Column(name = "polyprecording_id")
  private int polypRecordingId;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(
      name = "polypdataset_id", referencedColumnName = "polypdataset_id", insertable = false, updatable = false
    ),
    @JoinColumn(
      name = "polyp_id", referencedColumnName = "polyp_id", insertable = false, updatable = false
    )
  })
  private PolypInDataset polypInDataset;

  @ManyToOne
  @JoinColumns({
    @JoinColumn(
      name = "polyprecording_id", referencedColumnName = "id", insertable = false, updatable = false
    ),
    @JoinColumn(
      name = "polyp_id", referencedColumnName = "polyp_id", insertable = false, updatable = false
    )
  })
  private PolypRecording polypRecording;

  ReviewedPolypRecording() {}

  public ReviewedPolypRecording(PolypInDataset polypInDataset, PolypRecording polypRecording) {
    this.polypInDataset = polypInDataset;
    this.polypRecording = polypRecording;

    this.polypDatasetId = this.polypInDataset.getPolypDataset().getId();
    this.polypId = this.polypInDataset.getPolyp().getId();
    this.polypRecordingId = this.polypRecording.getId();
  }

  public PolypInDataset getPolypInDataset() {
    return polypInDataset;
  }

  public Polyp getPolyp() {
    return polypInDataset.getPolyp();
  }

  public PolypDataset getPolypDataset() {
    return polypInDataset.getPolypDataset();
  }

  public PolypRecording getPolypRecording() {
    return polypRecording;
  }

  @Override
  public int hashCode() {
    return Objects.hash(polypDatasetId, polypId, polypRecordingId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ReviewedPolypRecording other = (ReviewedPolypRecording) obj;
    return Objects.equals(polypDatasetId, other.polypDatasetId) && Objects.equals(polypId, other.polypId)
      && polypRecordingId == other.polypRecordingId;
  }

}
