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
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.service.spi.exploration.ExplorationService;
import org.sing_group.piba.service.spi.polyp.PolypService;
import org.sing_group.piba.service.spi.videomodification.VideoModificationService;

@Stateless
@PermitAll
public class DefaultExplorationService implements ExplorationService {

  @Inject
  private PolypService polypService;

  @Inject
  private VideoModificationService videoModificationService;

  @Inject
  private ExplorationDAO explorationDao;

  @Override
  public Exploration getExploration(String id) {
    return explorationDao.getExploration(id);
  }

  @Override
  public Stream<Exploration> getExplorations(int page, int pageSize, Patient patient) {
    return explorationDao.getExplorations(page, pageSize, patient);
  }

  @Override
  public int numExplorations() {
    return explorationDao.numExplorations();
  }

  @Override
  public int numExplorationsByPatient(Patient patient) {
    return explorationDao.numExplorationsByPatient(patient);
  }

  @Override
  public Exploration create(Exploration exploration) {
    return explorationDao.create(exploration);
  }

  @Override
  public Exploration edit(Exploration exploration) {
    if (exploration.isConfirmed()) {
      getPolyps(exploration).filter(polyp -> !polyp.isConfirmed()).forEach(polyp -> {
        polyp.setConfirmed(true);
        this.polypService.edit(polyp);
      });

      getVideos(exploration).forEach(
        video -> this.videoModificationService.getVideoModification(video)
          .filter(videoModification -> !videoModification.isConfirmed())
          .forEach(videoModification -> {
            videoModification.setConfirmed(true);
            this.videoModificationService.edit(videoModification);
          })
      );
    }
    return explorationDao.edit(exploration);
  }

  @Override
  public Stream<Polyp> getPolyps(Exploration exploration) {
    return explorationDao.getPolyps(exploration);
  }

  @Override
  public Stream<Video> getVideos(Exploration exploration) {
    return explorationDao.getVideos(exploration);
  }

  @Override
  public void delete(Exploration exploration) {
    explorationDao.delete(exploration);
  }

}
