package org.sing_group.piba.service.patient;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.patient.PatientDAO;
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

}
