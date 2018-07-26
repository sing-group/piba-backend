package org.sing_group.piba.service.spi.exploration;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.exploration.Exploration;

public interface ExplorationService {
  public Exploration getExploration(String id);

  public Stream<Exploration> getExplorations();
  public Exploration create(Exploration exploration);
}
