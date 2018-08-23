package org.sing_group.piba.service.exploration;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.service.spi.exploration.ExplorationService;

@Stateless
@PermitAll
public class DefaultExplorationService implements ExplorationService {

  @Inject
  private ExplorationDAO explorationDao;

  @Override
  public Exploration getExploration(String id) {
    return explorationDao.getExploration(id);
  }

  @Override
  public Stream<Exploration> getExplorations() {
    return explorationDao.getExplorations();
  }

  @Override
  public Exploration create(Exploration exploration) {
    return explorationDao.create(exploration);
  }

  @Override
  public Exploration edit(Exploration exploration) {
    return explorationDao.edit(exploration);
  }

  @Override
  public Stream<Polyp> getPolyps(Exploration exploration) {
    return explorationDao.getPolyps(exploration);
  }

  @Override
  public void delete(Exploration exploration) {
    explorationDao.delete(exploration);
  }

}