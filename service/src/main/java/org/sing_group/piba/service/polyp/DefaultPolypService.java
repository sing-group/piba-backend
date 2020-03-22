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

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.polyp.PolypDAO;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.service.spi.polyp.PolypService;
import org.sing_group.piba.service.spi.polyprecording.PolypRecordingService;

@Stateless
@PermitAll
public class DefaultPolypService implements PolypService {

  @Inject
  private PolypRecordingService polypRecordingService;

  @Inject
  private PolypDAO polypDao;

  @Override
  public Polyp getPolyp(String id) {
    return polypDao.getPolyp(id);
  }

  @Override
  public Polyp create(Polyp polyp) {
    return polypDao.create(polyp);
  }

  @Override
  public Polyp edit(Polyp polyp) {
    if (polyp.isConfirmed()) {
      this.polypRecordingService.listByPolyp(polyp).forEach(polypRecording -> {
        polypRecording.setConfirmed(true);
        this.polypRecordingService.edit(polypRecording);
      });
    }
    return polypDao.edit(polyp);
  }

  @Override
  public void delete(Polyp polyp) {
    polypDao.delete(polyp);
  }

}
