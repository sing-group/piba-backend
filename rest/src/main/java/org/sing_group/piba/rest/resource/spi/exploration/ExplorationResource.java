package org.sing_group.piba.rest.resource.spi.exploration;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

@Local
public interface ExplorationResource {
  public Response getExploration(String id);
  public Response getExplorations();
}
