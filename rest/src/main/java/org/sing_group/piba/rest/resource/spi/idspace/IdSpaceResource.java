package org.sing_group.piba.rest.resource.spi.idspace;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

@Local
public interface IdSpaceResource {
  public Response get(String id);

  public Response getIDSpaces();

}
