package org.sing_group.piba.service.spi.patient;

import javax.ejb.Local;

import org.sing_group.piba.domain.entities.patient.Patient;

@Local
public interface PatientService {
  public Patient create(Patient patient);

}
