/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato,
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
package org.sing_group.piba.rest.entity.mapper.spi;

import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.polyp.PolypDataset;
import org.sing_group.piba.rest.entity.polyp.PolypDatasetData;
import org.sing_group.piba.rest.entity.polyp.PolypDatasetEditionData;

public interface PolypDatasetMapper {
  public void setRequestURI(UriInfo requestURI);

  public PolypDatasetData toPolypDatasetData(PolypDataset dataset);

  public void assignPolypDatasetEditionData(PolypDataset polypDataset, PolypDatasetEditionData data);
}