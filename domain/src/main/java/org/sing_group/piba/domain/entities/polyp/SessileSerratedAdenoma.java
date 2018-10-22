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
package org.sing_group.piba.domain.entities.polyp;

import static java.util.Objects.requireNonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class SessileSerratedAdenoma extends PolypHistology {

  @Enumerated(EnumType.STRING)
  @Column(name = "ssadysplasia")
  private SsaDysplasingGrade dysplasingGrade;

  public enum SsaDysplasingGrade {
    WITHOUT, UNDEFINED, LOW, MODERATE, HIGH
  }

  SessileSerratedAdenoma() {}

  public SessileSerratedAdenoma(SsaDysplasingGrade dysplasingGrade) {
    super();
    setDysplasingGrade(dysplasingGrade);
  }

  public SsaDysplasingGrade getDysplasingGrade() {
    return dysplasingGrade;
  }

  public void setDysplasingGrade(SsaDysplasingGrade dysplasingGrade) {
    requireNonNull(dysplasingGrade);
    this.dysplasingGrade = dysplasingGrade;
  }

}
