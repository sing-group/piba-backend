package org.sing_group.piba.rest.resource.spi.user;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.piba.rest.entity.user.UserEditionData;

@Local
public interface UserResource {
  public Response role(String login);

  public Response create(UserEditionData userEditionData);

}
