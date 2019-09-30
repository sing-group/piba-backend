/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Florentino Fdez-Riverola, Alba Nogueira Rodríguez
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
package org.sing_group.piba.rest.resource.polyp;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.user.Role;
import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.rest.entity.mapper.spi.PolypMapper;
import org.sing_group.piba.rest.entity.polyp.PolypData;
import org.sing_group.piba.rest.entity.polyp.PolypEditionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.mapper.SecurityExceptionMapper;
import org.sing_group.piba.rest.resource.spi.polyp.PolypResource;
import org.sing_group.piba.service.spi.exploration.ExplorationService;
import org.sing_group.piba.service.spi.polyp.PolypService;
import org.sing_group.piba.service.spi.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("polyp")
@Api(value = "polyp")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultPolypResource implements PolypResource {

  @Inject
  private PolypService service;

  @Inject
  private ExplorationService explorationService;

  @Inject
  private UserService userService;

  @Inject
  private PolypMapper polypMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.polypMapper.setRequestURI(this.uriInfo);
  }

  @Path("{id}")
  @GET
  @ApiOperation(value = "Return the data of a polyp.", response = PolypData.class, code = 200)
  @ApiResponses(@ApiResponse(code = 400, message = "Unknown polyp: {id}"))
  @Override
  public Response getPolyp(@PathParam("id") String id) {
    return Response.ok(this.polypMapper.toPolypData(this.service.getPolyp(id))).build();
  }

  @POST
  @ApiOperation(
    value = "Creates a new polyp.", response = PolypData.class, code = 201
  )
  @Override
  public Response create(PolypEditionData polypEditionData) {
    Exploration exploration = getExploration(polypEditionData);
    checkPolypName(polypEditionData);

    Polyp polyp =
      new Polyp(
        polypEditionData.getName(), polypEditionData.getSize(), polypEditionData.getLocation(),
        polypEditionData.getWasp(), polypEditionData.getNice(),
        polypEditionData.getLst(), polypEditionData.getParisPrimary(), polypEditionData.getParisSecondary(),
        polypMapper.toPolypHistology(polypEditionData.getHistology()), polypEditionData.getObservation(), exploration, false
      );
    polyp = this.service.create(polyp);

    return Response.created(UriBuilder.fromResource(DefaultPolypResource.class).path(polyp.getId()).build())
      .entity(polypMapper.toPolypData(polyp)).build();
  }

  @Path("{id}")
  @PUT
  @ApiOperation(
    value = "Modifies an existing polyp", response = PolypData.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown polyp: {id}"),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response edit(@PathParam("id") String id, PolypEditionData polypEditionData) {
    checkPolypName(polypEditionData);
    Polyp polyp = this.service.getPolyp(id);
    if (isPolypEditable(polyp, polypEditionData)) {
      this.polypMapper.assignPolypEditionData(polyp, polypEditionData);
      return Response.ok(this.polypMapper.toPolypData(this.service.edit(polyp)))
        .build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  @PUT
  @ApiOperation(
    value = "Modifies all existing polyps in the array", response = PolypData.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown polyp."),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response editAll(PolypEditionData[] polypsEditionData) {
    if (
      Arrays.stream(polypsEditionData).allMatch(
        polypEditionData -> isPolypEditable(this.service.getPolyp(polypEditionData.getId()), polypEditionData)
      )
    ) {
      List<Polyp> editedPolyps = new ArrayList<Polyp>();
      Arrays.stream(polypsEditionData).forEach(polypEditionData -> {
        checkPolypName(polypEditionData);
        Polyp polyp = this.service.getPolyp(polypEditionData.getId());
        this.polypMapper.assignPolypEditionData(polyp, polypEditionData);
        editedPolyps.add(this.service.edit(polyp));
      });
      return Response.ok(
        editedPolyps.stream().map(this.polypMapper::toPolypData).toArray(PolypData[]::new)
      ).build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  @DELETE
  @Path("{id}")
  @ApiOperation(
    value = "Deletes an existing polyp.", code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown polyp: {id}"),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response delete(@PathParam("id") String id) {
    Polyp polyp = this.service.getPolyp(id);
    if (!polyp.isConfirmed()) {
      this.service.delete(polyp);
      return Response.ok().build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  private Exploration getExploration(PolypEditionData polypEditionData) {
    return this.explorationService.getExploration(polypEditionData.getExploration());
  }

  private void checkPolypName(PolypEditionData polypEditionData) {
    for (Polyp p : getExploration(polypEditionData).getPolyps()) {
      if (polypEditionData.getName().equals(p.getName()) && !polypEditionData.getId().equals(p.getId())) {
        throw new IllegalArgumentException("The polyp " + p.getName() + " already exists in this exploration");
      }
    }
  }

  private boolean isEndoscopist() {
    User currentUser = this.userService.getCurrentUser();
    return currentUser.getRole().equals(Role.ENDOSCOPIST);
  }

  private boolean isPolypEditable(Polyp polyp, PolypEditionData polypEditionData) {
    return (!polyp.isConfirmed() && polypEditionData.isConfirmed() && isEndoscopist())
      || (!polyp.isConfirmed() && !polypEditionData.isConfirmed());
  }

}
