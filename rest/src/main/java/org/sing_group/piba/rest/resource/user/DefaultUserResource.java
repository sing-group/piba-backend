package org.sing_group.piba.rest.resource.user;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.piba.domain.entities.user.Role;
import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.piba.rest.entity.user.UserData;
import org.sing_group.piba.rest.entity.user.UserEditionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.mapper.SecurityExceptionMapper;
import org.sing_group.piba.rest.resource.spi.user.UserResource;
import org.sing_group.piba.service.spi.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RolesAllowed({
  "ADMIN", "USER"
})
@Path("user")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Api("user")
@Stateless
@Default
@CrossDomain
public class DefaultUserResource implements UserResource {

  @Inject
  private UserService userService;

  @Inject
  private UserMapper userMapper;

  @GET
  @Path("{login}/role")
  @ApiOperation(
    value = "Checks the provided credentials", response = Role.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 200, message = "successful operation"),
    @ApiResponse(code = 401, message = SecurityExceptionMapper.UNAUTHORIZED_MESSAGE)
  })
  @Override
  public Response role(@PathParam("login") String login) {
    User currentUser = this.userService.getCurrentUser();
    if (!login.equals(currentUser.getLogin())) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    return Response.ok(
      currentUser.getRole()
    ).build();
  }

  @POST
  @RolesAllowed("ADMIN")
  @ApiOperation(
    value = "Creates a new user.", response = UserData.class, code = 201
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Entity already exists")
  )
  @Override
  public Response create(UserEditionData userEditionData) {
    User user = new User(userEditionData.getLogin(), userEditionData.getPassword(), userEditionData.getRole());
    user = this.userService.create(user);
    return Response.created(UriBuilder.fromResource(DefaultUserResource.class).path(user.getLogin()).build())
      .entity(userMapper.toUserData(user)).build();
  }

}
