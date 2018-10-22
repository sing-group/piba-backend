/*-
 * #%L
 * REST
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
package org.sing_group.piba.rest.entity.polyp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.domain.entities.polyp.Adenoma.AdenomaDysplasingGrade;
import org.sing_group.piba.domain.entities.polyp.Adenoma.AdenomaType;
import org.sing_group.piba.domain.entities.polyp.SessileSerratedAdenoma.SsaDysplasingGrade;
import org.sing_group.piba.domain.entities.polyp.TraditionalSerratedAdenoma.TsaDysplasingGrade;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "polyp-histology-edition-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "polyp-histology-edition-data", description = "Information of a histology of the polyp for edition.")
public class PolypHistologyEditionData {

  private String id;
  private PolypType polypType;
  private AdenomaType adenomaType;
  private AdenomaDysplasingGrade adenomaDysplasingGrade;
  private SsaDysplasingGrade ssaDysplasingGrade;
  private TsaDysplasingGrade tsaDysplasingGrade;

  public String getId() {
    return id;
  }

  public PolypType getPolypType() {
    return polypType;
  }

  public AdenomaType getAdenomaType() {
    return adenomaType;
  }

  public AdenomaDysplasingGrade getAdenomaDysplasingGrade() {
    return adenomaDysplasingGrade;
  }

  public SsaDysplasingGrade getSsaDysplasingGrade() {
    return ssaDysplasingGrade;
  }

  public TsaDysplasingGrade getTsaDysplasingGrade() {
    return tsaDysplasingGrade;
  }

}
