package org.sing_group.piba.domain.dao.spi.patient;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.patient.Patient;

public interface PatientDAO {

  public Patient create(Patient patient);

  public Stream<Patient> getPatients();

  public Stream<Patient> searchBy(String value);
  
  public Patient get(String id);

}
