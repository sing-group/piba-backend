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
package org.sing_group.piba.service.spi.exploration;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.video.Video;

public interface ExplorationService {
  public Exploration getExploration(String id);

  public Stream<Exploration> listExplorations(int page, int pageSize, Patient patient);

  public int countExplorations();

  public int countExplorationsByPatient(Patient patient);

  public Exploration create(Exploration exploration);

  public Exploration edit(Exploration exploration);

  public Stream<Polyp> listPolypsOfExploration(Exploration exploration);

  public Stream<Video> getVideos(Exploration exploration);

  public void delete(Exploration exploration);

}
