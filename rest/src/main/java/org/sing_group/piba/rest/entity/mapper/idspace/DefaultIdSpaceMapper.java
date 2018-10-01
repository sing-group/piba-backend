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
package org.sing_group.piba.rest.entity.mapper.idspace;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.idspace.IdSpace;
import org.sing_group.piba.rest.entity.idspace.IdSpaceData;
import org.sing_group.piba.rest.entity.idspace.IdSpaceEditionData;
import org.sing_group.piba.rest.entity.mapper.spi.idspace.IdSpaceMapper;

@Default
public class DefaultIdSpaceMapper implements IdSpaceMapper {

  @Override
  public void setRequestURI(UriInfo requestURI) {}

  @Override
  public IdSpaceData toIDSpaceData(IdSpace idSpace) {
    return new IdSpaceData(idSpace.getId(), idSpace.getName());
  }

  @Override
  public void assignIdSpaceEditionData(IdSpace idSpace, IdSpaceEditionData idSpaceEditionData) {
    idSpace.setName(idSpaceEditionData.getName());
  }

}
