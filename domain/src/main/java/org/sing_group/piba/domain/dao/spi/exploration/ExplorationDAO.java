package org.sing_group.piba.domain.dao.spi.exploration;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.exploration.Exploration;

public interface ExplorationDAO {
  public Exploration getExploration(String id);

  public Stream<Exploration> getExplorations();

  public Exploration create(Exploration exploration);
}
