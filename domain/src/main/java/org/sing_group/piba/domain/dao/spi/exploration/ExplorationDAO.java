package org.sing_group.piba.domain.dao.spi.exploration;

import org.sing_group.piba.domain.entities.exploration.Exploration;

public interface ExplorationDAO {
  public Exploration getExploration(String id);
}
