package org.sing_group.piba.rest.resource.exploration;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.rest.entity.exploration.ExplorationData;
import org.sing_group.piba.rest.entity.mapper.spi.exploration.ExplorationMapper;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.exploration.ExplorationResource;
import org.sing_group.piba.service.spi.exploration.ExplorationService;

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
@CrossDomain
public class DefaultExplorationResource implements ExplorationResource {

  @Inject
  private ExplorationService service;

  @Inject
  private ExplorationMapper explorationMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.explorationMapper.setRequestURI(this.uriInfo);
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
    value = "Return the data of all explorations.", response = ExplorationData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response getExplorations() {
    return Response.ok(
      this.service.getExplorations().map(this.explorationMapper::toExplorationData).toArray(ExplorationData[]::new)
    ).build();
  }

  @POST
  @ApiOperation(
    value = "Creates a new exploration.", response = ExplorationData.class, code = 201
  )
  @Override
  public Response create(ExplorationData explorationData) {
    Exploration exploration = new Exploration(explorationData.getLocation(), explorationData.getDate());
    exploration = this.service.create(exploration);

    return Response.created(UriBuilder.fromResource(DefaultExplorationResource.class).path(exploration.getId()).build())
      .entity(explorationMapper.toExplorationData(exploration)).build();
  }

  @PUT
  @ApiOperation(
    value = "Modifies an existing exploration", response = ExplorationData.class, code = 200
  )
  @Override
  public Response edit(ExplorationData explorationData) {
    return Response.ok(this.service.edit(explorationMapper.toExploration(explorationData))).build();
  }

}
