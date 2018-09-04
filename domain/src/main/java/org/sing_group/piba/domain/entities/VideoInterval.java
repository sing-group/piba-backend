package org.sing_group.piba.domain.entities;

import static java.util.Objects.requireNonNull;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class VideoInterval {

  @Column(nullable = false)
  private Integer start;
  @Column(nullable = false)
  private Integer end;

  public Integer getStart() {
    return start;
  }

  public void setStart(Integer start) {
    requireNonNull(start, "start of video can not be null");
    this.start = start;
  }

  public Integer getEnd() {
    return end;
  }

  public void setEnd(Integer end) {
    requireNonNull(end, "end of video can not be null");
    this.end = end;
  }

  public void checkInterval(Integer start, Integer end) {
    if (start > end) {
      throw new IllegalArgumentException("start can not be lower than final");
    }
  }

}
