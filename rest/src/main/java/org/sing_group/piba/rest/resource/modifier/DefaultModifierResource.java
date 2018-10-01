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
import javax.ws.rs.PathParam;
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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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

  @Path("{id}")
  @GET
  @ApiOperation(
    value = "Return the data of a modifier.", response = ModifierData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown modifier: {id}")
  )
  @Override
  public Response getModifier(
    @PathParam("id") String id
  ) {
    return Response
      .ok(this.modifierMapper.toModifierData(this.service.get(id)))
      .build();
  }

}
