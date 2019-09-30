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
package org.sing_group.piba.rest.resource.exploration;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.stream.Stream;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.user.Role;
import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.rest.entity.exploration.ExplorationData;
import org.sing_group.piba.rest.entity.exploration.ExplorationEditionData;
import org.sing_group.piba.rest.entity.mapper.spi.ExplorationMapper;
import org.sing_group.piba.rest.entity.mapper.spi.PolypMapper;
import org.sing_group.piba.rest.entity.polyp.PolypData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.mapper.SecurityExceptionMapper;
import org.sing_group.piba.rest.resource.spi.exploration.ExplorationResource;
import org.sing_group.piba.service.spi.exploration.ExplorationService;
import org.sing_group.piba.service.spi.patient.PatientService;
import org.sing_group.piba.service.spi.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("exploration")
@Api(value = "exploration")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain(allowedHeaders = "X-Pagination-Total-Items")
public class DefaultExplorationResource implements ExplorationResource {

  @Inject
  private ExplorationService service;

  @Inject
  private PatientService patientService;

  @Inject
  private UserService userService;

  @Inject
  private ExplorationMapper explorationMapper;

  @Inject
  private PolypMapper polypMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.explorationMapper.setRequestURI(this.uriInfo);
    this.polypMapper.setRequestURI(this.uriInfo);
  }

  @Path("{id}")
  @GET
  @ApiOperation(
    value = "Return the data of a exploration.", response = ExplorationData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown exploration: {id}")
  )
  @Override
  public Response getExploration(
    @PathParam("id") String id
  ) {
    return Response
      .ok(this.explorationMapper.toExplorationData(this.service.getExploration(id)))
      .build();
  }

  @GET
  @ApiOperation(
    value = "Return the data of all explorations or explorations of a specified patient and ID Space.", response = ExplorationData.class, responseContainer = "List", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Invalid page or pageSize. They must be an integer.")
  )
  @Override
  public Response getExplorations(
    @QueryParam("patient") String patientID, @QueryParam("idspace") String idSpace, @QueryParam(
      "page"
    ) String page,
    @QueryParam("pageSize") String pageSize
  ) {
    Patient patient = null;
    int numExplorations, pageInt, pageSizeInt;
    if (patientID == null || patientID.equals("") || idSpace == null || idSpace.equals("")) {
      numExplorations = this.service.numExplorations();
    } else {
      patient = this.patientService.getPatientBy(patientID, idSpace);
      numExplorations = this.service.numExplorationsByPatient(patient);
    }

    try {
      pageInt = Integer.parseInt(page);
      pageSizeInt = Integer.parseInt(pageSize);
    } catch (NumberFormatException exc) {
      throw new IllegalArgumentException("Invalid page or pageSize. They must be an integer.");
    }

    return Response.ok(
      this.service.getExplorations(pageInt, pageSizeInt, patient).map(this.explorationMapper::toExplorationData).toArray(ExplorationData[]::new)
    ).header("X-Pagination-Total-Items", numExplorations).build();
  }

  @POST
  @ApiOperation(
    value = "Creates a new exploration.", response = ExplorationData.class, code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown exploration: {id}")
  )
  @Override
  public Response create(ExplorationEditionData explorationEditionData) {
    Patient patient = this.patientService.get(explorationEditionData.getPatient());
    Exploration exploration =
      new Exploration(
        explorationEditionData.getTitle(), explorationEditionData.getLocation(),
        explorationEditionData.getExplorationDate(),
        patient, false
      );
    exploration = this.service.create(exploration);

    return Response.created(UriBuilder.fromResource(DefaultExplorationResource.class).path(exploration.getId()).build())
      .entity(explorationMapper.toExplorationData(exploration)).build();
  }

  @Path("{id}")
  @PUT
  @ApiOperation(
    value = "Modifies an existing exploration", response = ExplorationData.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown exploration: {id}"),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response edit(@PathParam("id") String id, ExplorationEditionData explorationEditionData) {

    Exploration exploration = this.service.getExploration(id);
    if (isExplorationEditable(exploration, explorationEditionData)) {
      this.explorationMapper.assignExplorationEditData(exploration, explorationEditionData);
      return Response.ok(this.explorationMapper.toExplorationData(this.service.edit(exploration))).build();

    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  @GET
  @Path("{id}/polyps")
  @ApiOperation(
    value = "Returns the polyps of a specifies exploration.", responseContainer = "List", response = PolypData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown exploration: {id}")
  )
  @Override
  public Response getPolyps(@PathParam("id") String id) {
    Exploration exploration = this.service.getExploration(id);
    if (exploration != null) {
      final Stream<Polyp> list = this.service.getPolyps(exploration);
      final PolypData[] polyps = list.map(this.polypMapper::toPolypData).toArray(PolypData[]::new);
      return Response.ok(polyps).build();
    }
    throw new IllegalArgumentException("Unknown exploration: " + id);
  }

  @DELETE
  @Path("{id}")
  @ApiOperation(
    value = "Deletes an existing exploration.", code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown exploration: {id}"),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response delete(@PathParam("id") String id) {
    Exploration exploration = this.service.getExploration(id);
    if (!exploration.isConfirmed()) {
      this.service.delete(exploration);
      return Response.ok().build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  private boolean isEndoscopist() {
    User currentUser = this.userService.getCurrentUser();
    return currentUser.getRole().equals(Role.ENDOSCOPIST);
  }

  private boolean isExplorationEditable(Exploration exploration, ExplorationEditionData explorationEditionData) {
    return (!exploration.isConfirmed() && explorationEditionData.isConfirmed() && isEndoscopist())
      || (!exploration.isConfirmed() && !explorationEditionData.isConfirmed());
  }

}
