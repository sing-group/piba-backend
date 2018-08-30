package org.sing_group.piba.rest.entity.mapper.patient;

import static org.sing_group.piba.rest.entity.UuidAndUri.fromEntities;

import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.rest.entity.mapper.spi.patient.PatientMapper;
import org.sing_group.piba.rest.entity.patient.PatientData;
import org.sing_group.piba.rest.entity.patient.PatientEditionData;
import org.sing_group.piba.rest.resource.patient.DefaultPatientResource;

public class DefaultPatientMapper implements PatientMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public PatientData toPatientData(Patient patient) {
    return new PatientData(
      patient.getId(), patient.getPatientID(), patient.getSex(), patient.getBirthdate(),
      fromEntities(requestURI, patient.getExplorations(), DefaultPatientResource.class)
    );
  }

  @Override
  public void assignPatientEditionData(Patient patient, PatientEditionData patientEditionData) {
    patient.setBirthdate(patientEditionData.getBirthdate());
    patient.setPatientID(patientEditionData.getPatientID());
    patient.setSex(patientEditionData.getSex());
  }

}
