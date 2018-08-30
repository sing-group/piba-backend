package org.sing_group.piba.rest.entity.mapper.spi.patient;

import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.rest.entity.patient.PatientData;
import org.sing_group.piba.rest.entity.patient.PatientEditionData;

public interface PatientMapper {
  public void setRequestURI(UriInfo requestURI);

  public PatientData toPatientData(Patient patient);

  public void assignPatientEditionData(Patient patient, PatientEditionData patientEditionData);
}
