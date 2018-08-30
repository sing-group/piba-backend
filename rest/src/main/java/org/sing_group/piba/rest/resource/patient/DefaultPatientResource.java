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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.rest.entity.mapper.spi.patient.PatientMapper;
import org.sing_group.piba.rest.entity.patient.PatientData;
import org.sing_group.piba.rest.entity.patient.PatientEditionData;
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
  public Response create(PatientEditionData patientEditionData) {
    Patient patient =
      this.service.create(
        new Patient(patientEditionData.getPatientID(), patientEditionData.getSex(), patientEditionData.getBirthdate())
      );

    return Response.created(UriBuilder.fromResource(DefaultPatientResource.class).path(patient.getId()).build())
      .entity(patientMapper.toPatientData(patient)).build();
  }

  @GET
  @Path("{id}")
  @ApiOperation(
    value = "Return the data of a patient.", response = PatientData.class, code = 200
  )
  @Override
  public Response getPatient(@PathParam("id") String id) {
    return Response.ok(this.patientMapper.toPatientData(this.service.get(id))).build();
  }

  @GET
  @ApiOperation(
    value = "Return the data of all patients or those whose id starts with an specified prefix.", response = PatientData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response getPatients(@QueryParam("patientIdStartsWith") String patientIdStartsWith) {
    if (patientIdStartsWith == null) {
      return Response.ok(this.service.getPatients().map(this.patientMapper::toPatientData).toArray(PatientData[]::new))
        .build();
    } else {
      return Response.ok(
        this.service.searchBy(patientIdStartsWith).map(this.patientMapper::toPatientData).toArray(PatientData[]::new)
      )
        .build();
    }
  }

  @GET
  @Path("patientID/{patientID}")
  @ApiOperation(
    value = "Return the data of a patient with the patientId received.", response = PatientData.class, code = 200
  )
  @Override
  public Response getPatientID(@PathParam("patientID") String patientID) {
    return Response.ok(this.patientMapper.toPatientData(this.service.getPatientID(patientID))).build();
  }

  @PUT
  @ApiOperation(
    value = "Modifies an existing patient", response = PatientData.class, code = 200
  )
  @Override
  public Response edit(PatientEditionData patientEditionData) {
    Patient patient = this.service.get(patientEditionData.getId());
    this.patientMapper.assignPatientEditionData(patient, patientEditionData);
    return Response.ok(this.patientMapper.toPatientData(this.service.edit(patient))).build();
  }

}
