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
package org.sing_group.piba.rest.resource.idspace;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
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
import org.sing_group.piba.rest.entity.exploration.ExplorationData;
import org.sing_group.piba.rest.entity.idspace.IdSpaceData;
import org.sing_group.piba.rest.entity.idspace.IdSpaceEditionData;
import org.sing_group.piba.rest.entity.mapper.spi.idspace.IdSpaceMapper;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.idspace.IdSpaceResource;
import org.sing_group.piba.service.spi.idspace.IdSpaceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RolesAllowed({
  "ADMIN", "USER"
})
@Path("idspace")
@Api(value = "idspace")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultIdSpaceResource implements IdSpaceResource {

  @Inject
  private IdSpaceService service;

  @Inject
  private IdSpaceMapper idSpaceMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.idSpaceMapper.setRequestURI(this.uriInfo);
  }

  @Path("{id}")
  @GET
  @ApiOperation(
    value = "Return the data of a id space.", response = ExplorationData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown ID Space: {id}")
  )
  @Override
  public Response get(@PathParam("id") String id) {
    return Response
      .ok(this.idSpaceMapper.toIDSpaceData(this.service.get(id)))
      .build();
  }

  @GET
  @ApiOperation(
    value = "Return the data of all id spaces.", response = IdSpaceData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response getIDSpaces() {
    return Response.ok(
      this.service.getIDSpaces().map(this.idSpaceMapper::toIDSpaceData).toArray(IdSpaceData[]::new)
    ).build();
  }

  @RolesAllowed("ADMIN")
  @POST
  @ApiOperation(
    value = "Creates a new ID Space.", response = IdSpaceData.class, code = 201
  )
  @Override
  public Response create(IdSpaceEditionData idSpaceEditionData) {
    IdSpace idSpace = this.service.create(new IdSpace(idSpaceEditionData.getName()));
    return Response.created(UriBuilder.fromResource(DefaultIdSpaceResource.class).path(idSpace.getId()).build())
      .entity(idSpaceMapper.toIDSpaceData(idSpace)).build();
  }

  @RolesAllowed("ADMIN")
  @Path("{id}")
  @PUT
  @ApiOperation(
    value = "Modifies an existing space", response = IdSpaceData.class, code = 200
  )
  @ApiResponses(@ApiResponse(code = 400, message = "Unknown ID Space: {id}"))
  @Override
  public Response edit(@PathParam("id") String id, IdSpaceEditionData idSpaceEditionData) {
    IdSpace idSpace = this.service.get(id);
    this.idSpaceMapper.assignIdSpaceEditionData(idSpace, idSpaceEditionData);
    return Response.ok(this.idSpaceMapper.toIDSpaceData(this.service.edit(idSpace)))
      .build();
  }

  @RolesAllowed("ADMIN")
  @DELETE
  @Path("{id}")
  @ApiOperation(
    value = "Deletes an existing ID Space.", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown ID Space: {id}")
  )
  @Override
  public Response delete(@PathParam("id") String id) {
    IdSpace idSpace = this.service.get(id);
    this.service.delete(idSpace);
    return Response.ok().build();
  }

}
