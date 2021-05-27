package org.sing_group.piba.domain.entities.polyp;

import java.io.Serializable;
import java.util.Objects;

public class PolypInDatasetId implements Serializable {
  private static final long serialVersionUID = 1L;

  private String polypId;

  private String polypDatasetId;

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
    PolypInDatasetId other = (PolypInDatasetId) obj;
    return Objects.equals(polypDatasetId, other.polypDatasetId) && Objects.equals(polypId, other.polypId);
  }

}
