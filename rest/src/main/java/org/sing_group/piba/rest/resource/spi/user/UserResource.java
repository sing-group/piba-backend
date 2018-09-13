package org.sing_group.piba.rest.resource.spi.user;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

@Local
public interface UserResource {
  public Response role(String login);
}
