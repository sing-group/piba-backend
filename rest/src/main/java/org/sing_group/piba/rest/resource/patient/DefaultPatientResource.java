/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato,
 * 			Florentino Fdez-Riverola, Alba Nogueira Rodríguez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.piba.rest.resource.patient;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.idspace.IdSpace;
import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.rest.entity.mapper.spi.patient.PatientMapper;
import org.sing_group.piba.rest.entity.patient.PatientData;
import org.sing_group.piba.rest.entity.patient.PatientEditionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.patient.PatientResource;
import org.sing_group.piba.service.spi.idspace.IdSpaceService;
import org.sing_group.piba.service.spi.patient.PatientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
  private IdSpaceService idSpaceService;

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
    IdSpace idSpace = idSpaceService.get(patientEditionData.getIdSpace());
    Patient patient =
      this.service.create(
        new Patient(
          patientEditionData.getPatientID(), patientEditionData.getSex(), patientEditionData.getBirthdate(), idSpace
        )
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
    value = "Return the data of all patients.", response = PatientData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response getPatients() {
    return Response.ok(this.service.getPatients().map(this.patientMapper::toPatientData).toArray(PatientData[]::new))
      .build();
  }

  @GET
  @Path("{patientID}/{idSpace}")
  @ApiOperation(
    value = "Return the data of a patient with the identifier of patient and space received.", response = PatientData.class, code = 200
  )
  @Override
  public Response getPatientBy(@PathParam("patientID") String patientID, @PathParam("idSpace") String idSpace) {
    return Response.ok(this.patientMapper.toPatientData(this.service.getPatientBy(patientID, idSpace))).build();
  }

  @Path("{id}")
  @PUT
  @ApiOperation(
    value = "Modifies an existing patient", response = PatientData.class, code = 200
  )
  @Override
  public Response edit(@PathParam("id") String id, PatientEditionData patientEditionData) {
    Patient patient = this.service.get(id);
    this.patientMapper.assignPatientEditionData(patient, patientEditionData);
    return Response.ok(this.patientMapper.toPatientData(this.service.edit(patient))).build();
  }

  @DELETE
  @Path("{id}")
  @ApiOperation(
    value = "Deletes an existing patient.", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown patient: {id}")
  )
  @Override
  public Response delete(@PathParam("id") String id) {
    Patient patient = this.service.get(id);
    this.service.delete(patient);
    return Response.ok().build();
  }

}
