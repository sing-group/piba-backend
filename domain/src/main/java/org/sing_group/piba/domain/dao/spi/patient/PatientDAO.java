package org.sing_group.piba.domain.dao.spi.patient;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.patient.Patient;

public interface PatientDAO {

  public Patient create(Patient patient);

  public Stream<Patient> getPatients();

}
