package org.sing_group.piba.rest.resource.spi.patient;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.piba.rest.entity.patient.PatientData;

@Local
public interface PatientResource {
  public Response create(PatientData patientData);

  public Response getPatients();

}
