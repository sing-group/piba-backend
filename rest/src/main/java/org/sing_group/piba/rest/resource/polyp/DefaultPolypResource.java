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

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.rest.entity.exploration.ExplorationData;
import org.sing_group.piba.rest.entity.mapper.spi.polyp.PolypMapper;
import org.sing_group.piba.rest.entity.polyp.PolypData;
import org.sing_group.piba.rest.entity.polyp.PolypEditionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.polyp.PolypResource;
import org.sing_group.piba.service.spi.exploration.ExplorationService;
import org.sing_group.piba.service.spi.polyp.PolypService;

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

  @GET
  @ApiOperation(
    value = "Return the data of all polyp.", response = PolypData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response listPolyp() {
    return Response.ok(this.service.getPolyps().map(this.polypMapper::toPolypData).toArray(PolypData[]::new))
      .build();
  }

  @POST
  @ApiOperation(
    value = "Creates a new polyp.", response = ExplorationData.class, code = 201
  )
  @Override
  public Response create(PolypEditionData polypEditionData) {
    Exploration exploration = this.explorationService.getExploration(polypEditionData.getExploration());
    Polyp polyp =
      new Polyp(
        polypEditionData.getName(), polypEditionData.getSize(), polypEditionData.getLocation(),
        polypEditionData.getWasp(), polypEditionData.getNice(),
        polypEditionData.getLst(), polypEditionData.getParis(),
        polypEditionData.getHistology(), exploration
      );
    polyp = this.service.create(polyp);

    return Response.created(UriBuilder.fromResource(DefaultPolypResource.class).path(polyp.getId()).build())
      .entity(polypMapper.toPolypData(polyp)).build();
  }

}
