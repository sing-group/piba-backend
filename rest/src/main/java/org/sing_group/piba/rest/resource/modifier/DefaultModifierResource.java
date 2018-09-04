package org.sing_group.piba.rest.resource.modifier;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.rest.entity.mapper.spi.modifier.ModifierMapper;
import org.sing_group.piba.rest.entity.modifier.ModifierData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.modifier.ModifierResource;
import org.sing_group.piba.service.spi.modifier.ModifierService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("modifier")
@Api(value = "modifier")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultModifierResource implements ModifierResource {

  @Inject
  private ModifierService service;

  @Inject
  private ModifierMapper modifierMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.modifierMapper.setRequestURI(this.uriInfo);
  }

  @GET
  @ApiOperation(
    value = "Return the data of all modifiers.", response = ModifierData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response getModifiers() {
    return Response
      .ok(this.service.getModifiers().map(this.modifierMapper::toModifierData).toArray(ModifierData[]::new))
      .build();
  }

}
