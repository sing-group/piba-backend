package org.sing_group.piba.service.patient;

import java.util.stream.Stream;

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

}
