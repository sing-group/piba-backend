package org.sing_group.piba.domain.dao.spi.patient;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.patient.Patient;

public interface PatientDAO {

  public Patient create(Patient patient);

  public Stream<Patient> getPatients();

  public Stream<Patient> searchBy(String patientIdStartsWith);

  public Patient get(String id);

  public Patient getPatientID(String patientID);

  public Patient edit(Patient patient);

  public void delete(Patient patient);
}
