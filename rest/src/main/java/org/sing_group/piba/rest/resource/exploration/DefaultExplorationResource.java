package org.sing_group.piba.rest.resource.exploration;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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

}
