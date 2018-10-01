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
package org.sing_group.piba.service.patient;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.patient.PatientDAO;
import org.sing_group.piba.domain.entities.idspace.IdSpace;
import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.service.spi.patient.PatientService;

@Stateless
@PermitAll
public class DefaultPatientService implements PatientService {

  @Inject
  private PatientDAO patientDAO;

  @Override
  public Patient create(Patient patient) {
    return patientDAO.create(patient);
  }

  @Override
  public Stream<Patient> getPatients() {
    return patientDAO.getPatients();
  }

  @Override
  public Patient get(String id) {
    return patientDAO.get(id);
  }

  @Override
  public Patient getPatientBy(String patientID, String idSpace) {
    return patientDAO.getPatientBy(patientID, idSpace);
  }

  @Override
  public Patient edit(Patient patient) {
    return patientDAO.edit(patient);
  }

  @Override
  public void delete(Patient patient) {
    patientDAO.delete(patient);
  }

  @Override
  public Stream<Patient> getPatientsBy(IdSpace idSpace) {
    return patientDAO.getPatientsBy(idSpace);
  }

}
