package org.sing_group.piba.service.spi.exploration;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.polyp.Polyp;

public interface ExplorationService {
  public Exploration getExploration(String id);

  public Stream<Exploration> getExplorations();

  public Exploration create(Exploration exploration);

  public Exploration edit(Exploration exploration);

  public Stream<Polyp> getPolyps(Exploration exploration);

  public void delete(Exploration exploration);
}
