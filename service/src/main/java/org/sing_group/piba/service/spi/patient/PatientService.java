package org.sing_group.piba.service.spi.patient;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.piba.domain.entities.patient.Patient;

@Local
public interface PatientService {
  public Patient create(Patient patient);

  public Stream<Patient> getPatients();
  
  public Stream<Patient> searchBy(String value);
  
  public Patient get(String id);

}
