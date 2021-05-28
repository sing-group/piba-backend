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

import java.io.Serializable;
import java.util.Objects;

public class ReviewedPolypRecordingId implements Serializable {
  private static final long serialVersionUID = 1L;

  private String polypDatasetId;

  private String polypId;

  private int polypRecordingId;

  public String getPolypDatasetId() {
    return polypDatasetId;
  }

  public String getPolypId() {
    return polypId;
  }

  public int getPolypRecordingId() {
    return polypRecordingId;
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
    ReviewedPolypRecordingId other = (ReviewedPolypRecordingId) obj;
    return Objects.equals(polypDatasetId, other.polypDatasetId) && Objects.equals(polypId, other.polypId)
      && polypRecordingId == other.polypRecordingId;
  }

}
