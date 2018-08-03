package org.sing_group.piba.rest.entity.mapper.spi.exploration;

import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.rest.entity.exploration.ExplorationData;
import org.sing_group.piba.rest.entity.exploration.ExplorationEditionData;

public interface ExplorationMapper {
  public void setRequestURI(UriInfo requestURI);

  public ExplorationData toExplorationData(Exploration exploration);

  
  public void assignExplorationEditData(Exploration exploration, ExplorationEditionData explorationEditionData);
}
