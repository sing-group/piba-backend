/*-
 * #%L
 * Service
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
package org.sing_group.piba.service.polyp;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.polyp.PolypDatasetDAO;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyp.PolypDataset;
import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;
import org.sing_group.piba.service.spi.polyp.PolypDatasetService;

@Stateless
@PermitAll
public class DefaultPolypDatasetService implements PolypDatasetService {
  @Inject
  private PolypDatasetDAO polypDatasetDao;
  
  @Override
  public PolypDataset getPolypDataset(String id) {
    return this.polypDatasetDao.getPolypDataset(id);
  }

  @Override
  public Stream<PolypDataset> listPolypDatasets(int page, int pageSize) {
    return this.polypDatasetDao.listPolypDatasets(page, pageSize);
  }
  
  @Override
  public Stream<Polyp> listPolypsInDatasets(String datasetId, int page, int pageSize) {
    return this.polypDatasetDao.listPolypsInDataset(datasetId, page, pageSize);
  }
  
  @Override
  public Stream<PolypRecording> listPolypRecordingsInDatasets(String datasetId, Integer page, Integer pageSize) {
    return this.polypDatasetDao.listPolypRecordingsInDatasets(datasetId, page, pageSize);
  }
  
  @Override
  public int countPolypDatasets() {
    return this.polypDatasetDao.countPolypDatasets();
  }
  
  @Override
  public int countPolypsInDatasets(String datasetId) {
    return this.polypDatasetDao.countPolypsInDataset(datasetId);
  }
  
  @Override
  public int countPolypRecordingsInDatasets(String datasetId) {
    return this.polypDatasetDao.countPolypRecordingsInDatasets(datasetId);
  }
  
  @Override
  public PolypDataset createPolypDataset(PolypDataset polypDataset) {
    return this.polypDatasetDao.createPolypDataset(polypDataset);
  }
  
  @Override
  public PolypDataset editPolypDataset(PolypDataset polypDataset) {
    return this.polypDatasetDao.editPolypDataset(polypDataset);
  }
  
  @Override
  public void deletePolypDataset(String datasetId) {
    this.polypDatasetDao.deletePolypDataset(datasetId);
  }
}
