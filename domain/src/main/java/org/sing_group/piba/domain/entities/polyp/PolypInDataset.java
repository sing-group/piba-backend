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
import java.util.Set;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "polypsindataset")
@IdClass(PolypInDatasetId.class)
public class PolypInDataset {

  @Id
  @Column(name = "polyp_id")
  private String polypId;

  @Id
  @Column(name = "polypdataset_id")
  private String polypDatasetId;

  @ManyToOne
  @JoinColumn(
    name = "polyp_id", referencedColumnName = "id", insertable = false, updatable = false
  )
  private Polyp polyp;

  @ManyToOne
  @JoinColumn(
    name = "polypdataset_id", referencedColumnName = "id", insertable = false, updatable = false
  )
  private PolypDataset polypDataset;

  @OneToMany(mappedBy = "polypInDataset", fetch = FetchType.LAZY)
  private Set<ReviewedPolypRecording> reviewedPolypRecordings;
  
  PolypInDataset() {}
  
  public PolypInDataset(Polyp polyp, PolypDataset polypDataset) {
    this.polyp = polyp;
    this.polypDataset = polypDataset;
    
    this.polypDatasetId = this.polypDataset.getId();
    this.polypId = this.polyp.getId();
  }

  public Polyp getPolyp() {
    return polyp;
  }

  public PolypDataset getPolypDataset() {
    return polypDataset;
  }
  
  public Stream<ReviewedPolypRecording> getReviewedPolypRecordings() {
    return reviewedPolypRecordings.stream();
  }

  @Override
  public int hashCode() {
    return Objects.hash(polypDatasetId, polypId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PolypInDataset other = (PolypInDataset) obj;
    return Objects.equals(polypDatasetId, other.polypDatasetId) && Objects.equals(polypId, other.polypId);
  }

}
