package org.sing_group.piba.rest.entity.mapper.exploration;

import javax.enterprise.inject.Default;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.rest.entity.exploration.ExplorationData;
import org.sing_group.piba.rest.entity.mapper.spi.exploration.ExplorationMapper;

@Default
public class DefaultExplorationMapper implements ExplorationMapper {

  @Override
  public ExplorationData toExplorationData(Exploration exploration) {
    return new ExplorationData(exploration.getId(), exploration.getLocation(), exploration.getDate());
  }

}
