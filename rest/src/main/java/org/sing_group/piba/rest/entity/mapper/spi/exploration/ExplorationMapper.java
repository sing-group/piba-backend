package org.sing_group.piba.rest.entity.mapper.spi.exploration;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.rest.entity.exploration.ExplorationData;

public interface ExplorationMapper {
  public ExplorationData toExplorationData(Exploration exploration);
}
