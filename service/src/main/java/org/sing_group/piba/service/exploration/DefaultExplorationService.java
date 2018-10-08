/*-
 * #%L
 * Service
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
package org.sing_group.piba.service.exploration;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.service.spi.exploration.ExplorationService;

@Stateless
@PermitAll
public class DefaultExplorationService implements ExplorationService {

  @Inject
  private ExplorationDAO explorationDao;

  @Override
  public Exploration getExploration(String id) {
    return explorationDao.getExploration(id);
  }

  @Override
  public Stream<Exploration> getExplorations() {
    return explorationDao.getExplorations();
  }

  @Override
  public Exploration create(Exploration exploration) {
    return explorationDao.create(exploration);
  }

  @Override
  public Exploration edit(Exploration exploration) {
    return explorationDao.edit(exploration);
  }

  @Override
  public Stream<Polyp> getPolyps(Exploration exploration) {
    return explorationDao.getPolyps(exploration);
  }

  @Override
  public void delete(Exploration exploration) {
    explorationDao.delete(exploration);
  }

  @Override
  public Stream<Exploration> getExplorationsBy(Patient patient) {
    return explorationDao.getExplorationsBy(patient);
  }

}
