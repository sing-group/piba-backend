package org.sing_group.piba.rest.resource.spi.patient;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.piba.rest.entity.patient.PatientEditionData;

@Local
public interface PatientResource {
  public Response create(PatientEditionData patientEditionData);

  public Response getPatients(String patientIdStartsWith);

  public Response getPatient(String id);

  public Response getPatientID(String patientID);
  
  public Response edit(PatientEditionData patientEditionData);

}
