/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato,
 *      Florentino Fdez-Riverola, Alba Nogueira Rodríguez
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
package org.sing_group.piba.rest.resource.user;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sing_group.piba.domain.entities.passwordrecovery.PasswordRecovery;
import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.user.UserPasswordRecoveryResource;
import org.sing_group.piba.service.spi.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("recovery")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Api("recovery")
@Stateless
@Default
@CrossDomain
public class DefaultUserPasswordRecoveryResource implements UserPasswordRecoveryResource {

  @Inject
  private UserService userService;
  
  //@Inject
  //private UserMapper userMapper;

  @POST
  @ApiOperation(
    value = "Sends an email to recover user password", code = 200
  )
  @Override
  public Response recoverPassword(User user) {
    if (!user.getEmail().equals("null@null.null")) {
      this.userService.recoverPassword(user.getEmail());
    }
    else {
      this.userService.recoverPassword(user.getLogin());
    }
    return Response.ok().build();
  }
  
  @PUT
  @Path("/password")
  @ApiOperation(
    value = "Changes user's password", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "No password recovery found or it's over date")
  )
  @Override
  public Response updatePasswordRecovery(PasswordRecovery newPassword) {
    this.userService.updatePasswordRecovery(newPassword);
    return Response.ok().build();
  }

}
