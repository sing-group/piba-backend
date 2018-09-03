package org.sing_group.piba.rest.resource.spi.patient;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.piba.rest.entity.patient.PatientEditionData;

@Local
public interface PatientResource {
  public Response create(PatientEditionData patientEditionData);

  public Response getPatients(String patientIdStartsWith, String idSpace);

  public Response getPatient(String id);

  public Response getPatientBy(String patientID, String idSpace);

  public Response edit(PatientEditionData patientEditionData);

  public Response delete(String id);

}
