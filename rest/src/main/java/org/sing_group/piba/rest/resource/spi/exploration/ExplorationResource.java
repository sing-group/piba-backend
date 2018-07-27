package org.sing_group.piba.rest.resource.spi.exploration;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.piba.rest.entity.exploration.ExplorationData;

@Local
public interface ExplorationResource {
  public Response getExploration(String id);

  public Response getExplorations();

  public Response create(ExplorationData explorationData);

  public Response edit(ExplorationData explorationData);
}
