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
