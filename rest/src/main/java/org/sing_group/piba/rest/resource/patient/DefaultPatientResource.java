package org.sing_group.piba.rest.resource.patient;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.rest.entity.mapper.spi.patient.PatientMapper;
import org.sing_group.piba.rest.entity.patient.PatientData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.patient.PatientResource;
import org.sing_group.piba.service.spi.patient.PatientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("patient")
@Api(value = "patient")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultPatientResource implements PatientResource {

  @Inject
  private PatientService service;

  @Inject
  private PatientMapper patientMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.patientMapper.setRequestURI(this.uriInfo);
  }

  @POST
  @ApiOperation(
    value = "Creates a new patient.", response = PatientData.class, code = 201
  )
  @Override
  public Response create(PatientData patientData) {
    Patient patient =
      this.service.create(new Patient(patientData.getPatientID(), patientData.getSex(), patientData.getBirthdate()));

    return Response.created(UriBuilder.fromResource(DefaultPatientResource.class).path(patient.getId()).build())
      .entity(patientMapper.toPatientData(patient)).build();
  }

  @GET
  @ApiOperation(
    value = "Return the data of all patients.", response = PatientData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response getPatients() {
    return Response.ok(this.service.getPatients().map(this.patientMapper::toPatientData).toArray(PatientData[]::new))
      .build();
  }

}
