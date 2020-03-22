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
package org.sing_group.piba.service.idspace;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.idspace.IdSpaceDAO;
import org.sing_group.piba.domain.entities.idspace.IdSpace;
import org.sing_group.piba.service.spi.idspace.IdSpaceService;
import org.sing_group.piba.service.spi.patient.PatientService;

@Stateless
@PermitAll
public class DefaultIdSpaceService implements IdSpaceService {

  @Inject
  private IdSpaceDAO idSpaceDAO;

  @Inject
  private PatientService patientService;

  @Override
  public IdSpace get(String id) {
    return idSpaceDAO.get(id);
  }

  @Override
  public Stream<IdSpace> listIDSpaces() {
    return idSpaceDAO.listIDSpaces();
  }

  @Override
  public IdSpace create(IdSpace idSpace) {
    return idSpaceDAO.create(idSpace);
  }

  @Override
  public IdSpace edit(IdSpace idSpace) {
    return idSpaceDAO.edit(idSpace);
  }

  @Override
  public void delete(IdSpace idSpace) {
    if (this.patientService.getPatientsBy(idSpace).count() > 0) {
      throw new IllegalArgumentException("Can not remove space with patients");
    }
    idSpaceDAO.delete(idSpace);
  }

}
