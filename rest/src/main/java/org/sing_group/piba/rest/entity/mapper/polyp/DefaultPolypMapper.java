/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Florentino Fdez-Riverola, Alba Nogueira Rodríguez
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
package org.sing_group.piba.rest.entity.mapper.polyp;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.polyp.Adenoma;
import org.sing_group.piba.domain.entities.polyp.Adenoma.AdenomaDysplasingGrade;
import org.sing_group.piba.domain.entities.polyp.Adenoma.AdenomaType;
import org.sing_group.piba.domain.entities.polyp.Hyperplastic;
import org.sing_group.piba.domain.entities.polyp.Invasive;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyp.PolypHistology;
import org.sing_group.piba.domain.entities.polyp.SessileSerratedAdenoma;
import org.sing_group.piba.domain.entities.polyp.SessileSerratedAdenoma.SsaDysplasingGrade;
import org.sing_group.piba.domain.entities.polyp.TraditionalSerratedAdenoma;
import org.sing_group.piba.domain.entities.polyp.TraditionalSerratedAdenoma.TsaDysplasingGrade;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.mapper.spi.polyp.PolypMapper;
import org.sing_group.piba.rest.entity.polyp.PolypData;
import org.sing_group.piba.rest.entity.polyp.PolypEditionData;
import org.sing_group.piba.rest.entity.polyp.PolypHistologyData;
import org.sing_group.piba.rest.entity.polyp.PolypHistologyEditionData;
import org.sing_group.piba.rest.entity.polyp.PolypType;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;
import org.sing_group.piba.service.spi.exploration.ExplorationService;

@Default
public class DefaultPolypMapper implements PolypMapper {

  private UriInfo requestURI;

  @Inject
  private ExplorationService explorationService;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public PolypData toPolypData(Polyp polyp) {
    return new PolypData(
      polyp.getId(), polyp.getName(), polyp.getSize(), polyp.getLocation(), polyp.getWasp(),
      polyp.getNice(), polyp.getLst(), polyp.getParisPrimary(), polyp.getParisSecondary(),
      toPolypHistologyData(polyp.getHistology()),
      polyp.getObservation(), UuidAndUri.fromEntity(requestURI, polyp.getExploration(), DefaultVideoResource.class)
    );
  }

  private PolypHistologyData toPolypHistologyData(PolypHistology data) {
    if (data == null) {
      return new PolypHistologyData();
    }
    AdenomaType adenomaType = null;
    AdenomaDysplasingGrade adenomaDysplasingGrade = null;
    SsaDysplasingGrade ssaDysplasingGrade = null;
    TsaDysplasingGrade tsaDysplasingGrade = null;
    PolypType polypType;
    switch (data.getClass().getSimpleName()) {
      case "Adenoma":
        adenomaType = ((Adenoma) data).getType();
        adenomaDysplasingGrade = AdenomaDysplasingGrade.valueOf(((Adenoma) data).getDysplasingGrade().name());
        polypType = PolypType.ADENOMA;
        break;
      case "Invasive":
        polypType = PolypType.INVASIVE;
        break;
      case "Hyperplastic":
        polypType = PolypType.HYPERPLASTIC;
        break;
      case "SessileSerratedAdenoma":
        ssaDysplasingGrade = ((SessileSerratedAdenoma) data).getDysplasingGrade();
        polypType = PolypType.SESSILE_SERRATED_ADENOMA;
        break;
      case "TraditionalSerratedAdenoma":
        tsaDysplasingGrade = ((TraditionalSerratedAdenoma) data).getDysplasingGrade();
        polypType = PolypType.TRADITIONAL_SERRATED_ADENOMA;
        break;
      default:
        throw new IllegalArgumentException("Unknown polyp histology type: " + data.getClass());
    }
    return new PolypHistologyData(
      data.getId(), polypType, adenomaType,
      adenomaDysplasingGrade, ssaDysplasingGrade, tsaDysplasingGrade
    );
  }

  @Override
  public void assignPolypEditionData(Polyp polyp, PolypEditionData polypEditionData) {
    if (
      polyp.getHistology() != null &&
        polyp.getHistology().getClass().getSimpleName().equalsIgnoreCase(
          polypEditionData.getHistology()
            .getPolypType().name()
        )
    ) {
      assignPolypHistologyEditionData(polyp.getHistology(), polypEditionData.getHistology());
    } else {
      polyp.setHistology(toPolypHistology(polypEditionData.getHistology()));
    }
    polyp.setLocation(polypEditionData.getLocation());
    polyp.setLst(polypEditionData.getLst());
    polyp.setName(polypEditionData.getName());
    polyp.setNice(polypEditionData.getNice());
    polyp.setParisPrimary(polypEditionData.getParisPrimary());
    polyp.setParisSecondary(polypEditionData.getParisSecondary());
    polyp.setWasp(polypEditionData.getWasp());
    polyp.setSize(polypEditionData.getSize());
    polyp.setObservation(polypEditionData.getObservation());
    polyp.setExploration(
      polypEditionData.getExploration() == null ? null
        : this.explorationService.getExploration(polypEditionData.getExploration())
    );
  }

  private void assignPolypHistologyEditionData(PolypHistology histology, PolypHistologyEditionData data) {
    switch (data.getPolypType()) {
      case ADENOMA:
        ((Adenoma) histology).setType(data.getAdenomaType());
        ((Adenoma) histology).setDysplasingGrade(data.getAdenomaDysplasingGrade());
        break;
      case INVASIVE:
        break;
      case HYPERPLASTIC:
        break;
      case SESSILE_SERRATED_ADENOMA:
        ((SessileSerratedAdenoma) histology).setDysplasingGrade(data.getSsaDysplasingGrade());
        break;
      case TRADITIONAL_SERRATED_ADENOMA:
        ((TraditionalSerratedAdenoma) histology).setDysplasingGrade(data.getTsaDysplasingGrade());
        break;
      default:
        throw new IllegalArgumentException("Unknown polyp histology type: " + data.getClass());
    }
  }

  @Override
  public PolypHistology toPolypHistology(PolypHistologyEditionData data) {
    PolypHistology polypHistology = null;
    if (data != null) {
      switch (data.getPolypType()) {
        case ADENOMA:
          polypHistology = new Adenoma(data.getAdenomaType(), data.getAdenomaDysplasingGrade());
          break;
        case INVASIVE:
          polypHistology = new Invasive();
          break;
        case HYPERPLASTIC:
          polypHistology = new Hyperplastic();
          break;
        case SESSILE_SERRATED_ADENOMA:
          polypHistology = new SessileSerratedAdenoma(data.getSsaDysplasingGrade());
          break;
        case TRADITIONAL_SERRATED_ADENOMA:
          polypHistology = new TraditionalSerratedAdenoma(data.getTsaDysplasingGrade());
          break;
        default:
          throw new IllegalArgumentException("Unknown polyp histology type: " + data.getClass());
      }
    }
    return polypHistology;
  }
}
