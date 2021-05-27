package org.sing_group.piba.domain.entities.polyp;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "polypindataset")
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
