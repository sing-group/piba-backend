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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.mapper.spi.polyp.PolypMapper;
import org.sing_group.piba.rest.entity.polyp.PolypData;
import org.sing_group.piba.rest.entity.polyp.PolypEditionData;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;

@Default
public class DefaultPolypMapper implements PolypMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public PolypData toPolypData(Polyp polyp) {
    return new PolypData(
      polyp.getId(), polyp.getName(), polyp.getSize(), polyp.getLocation(), polyp.getWasp(),
      polyp.getNice(), polyp.getLst(), polyp.getParis(), polyp.getHistology(), linkExploration(polyp.getExploration())
    );
  }

  private UuidAndUri linkExploration(Exploration exploration) {
    return new UuidAndUri(
      exploration.getId(),
      requestURI.getBaseUriBuilder().path(
        UriBuilder.fromResource(DefaultVideoResource.class).path(exploration.getId()).build().toString()
      )
        .build()
    );
  }

  @Override
  public Polyp toPolyp(PolypEditionData polypEditionData, Exploration exploration) {
    return new Polyp(polypEditionData.getId(), polypEditionData.getName(), polypEditionData.getSize(), 
      polypEditionData.getLocation(), polypEditionData.getWasp(), polypEditionData.getNice(), 
      polypEditionData.getLst(), polypEditionData.getParis(), polypEditionData.getHistology(), 
      exploration);
  }
}
