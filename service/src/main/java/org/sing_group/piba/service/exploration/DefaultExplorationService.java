package org.sing_group.piba.service.exploration;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.piba.domain.entities.exploration.Exploration;
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

}
