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
package org.sing_group.piba.rest.entity.mapper;

import static org.sing_group.piba.rest.entity.UuidAndUri.fromEntities;
import static org.sing_group.piba.rest.entity.UuidAndUri.fromEntity;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.rest.entity.exploration.ExplorationData;
import org.sing_group.piba.rest.entity.exploration.ExplorationEditionData;
import org.sing_group.piba.rest.entity.mapper.spi.ExplorationMapper;
import org.sing_group.piba.rest.resource.patient.DefaultPatientResource;
import org.sing_group.piba.rest.resource.polyp.DefaultPolypResource;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;

@Default
public class DefaultExplorationMapper implements ExplorationMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public ExplorationData toExplorationData(Exploration exploration) {
    return new ExplorationData(
      exploration.getId(), exploration.getTitle(), exploration.getLocation(), exploration.getDate(),
      fromEntities(requestURI, exploration.getVideos(), DefaultVideoResource.class),
      fromEntities(requestURI, exploration.getPolyps(), DefaultPolypResource.class),
      fromEntity(requestURI, exploration.getPatient(), DefaultPatientResource.class)
    );
  }

  @Override
  public void assignExplorationEditData(Exploration exploration, ExplorationEditionData explorationEditionData) {
    exploration.setTitle(explorationEditionData.getTitle());
    exploration.setDate(explorationEditionData.getDate());
    exploration.setLocation(explorationEditionData.getLocation());
  }
}
