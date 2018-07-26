package org.sing_group.piba.domain.dao.exploration;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.piba.domain.entities.exploration.Exploration;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultExplorationDAO implements ExplorationDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, Exploration> dh;

  public DefaultExplorationDAO() {
    super();
  }

  public DefaultExplorationDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, Exploration.class, this.em);
  }

  @Override
  public Exploration getExploration(String id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown exploration: " + id));
  }

  @Override
  public Stream<Exploration> getExplorations() {
    return dh.list().stream();
  }

  @Override
  public Exploration create(Exploration exploration) {
    return this.dh.persist(exploration);
  }

}
