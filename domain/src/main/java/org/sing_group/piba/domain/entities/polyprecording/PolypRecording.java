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

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyprecording.PolypRecording.PolypRecordingId;
import org.sing_group.piba.domain.entities.video.Video;

@Entity
@Table(name = "polyprecording")
@IdClass(PolypRecordingId.class)
public class PolypRecording implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @ManyToOne
  private Polyp polyp;

  @Id
  @ManyToOne
  private Video video;

  @Column(nullable = false)
  private Integer start;
  @Column(nullable = false)
  private Integer end;

  PolypRecording() {}

  public PolypRecording(Polyp polyp, Video video, Integer start, Integer end) {
    this.polyp = polyp;
    this.video = video;
    setStart(start);
    setEnd(end);
    checkInterval(start, end);
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

  public Integer getStart() {
    return start;
  }

  public void setStart(Integer start) {
    requireNonNull(start, "start of polyp can not be null");
    this.start = start;
  }

  public Integer getEnd() {
    return end;
  }

  public void setEnd(Integer end) {
    requireNonNull(end, "end of polyp can not be null");
    this.end = end;
  }

  private void checkInterval(Integer start, Integer end) {
    if (start > end) {
      throw new IllegalArgumentException("start can not be lower than final");
    }
  }

  public static class PolypRecordingId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String polyp;
    private String video;

    public PolypRecordingId() {}

    public PolypRecordingId(String polyp, String video) {
      this.polyp = polyp;
      this.video = video;
    }
    
    public PolypRecordingId(Polyp polyp, Video video) {
      this.polyp = polyp.getId();
      this.video = video.getId();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((polyp == null) ? 0 : polyp.hashCode());
      result = prime * result + ((video == null) ? 0 : video.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      PolypRecordingId other = (PolypRecordingId) obj;
      if (polyp == null) {
        if (other.polyp != null)
          return false;
      } else if (!polyp.equals(other.polyp))
        return false;
      if (video == null) {
        if (other.video != null)
          return false;
      } else if (!video.equals(other.video))
        return false;
      return true;
    }

  }
}
