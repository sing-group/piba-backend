package org.sing_group.piba.domain.dao.modifier;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.modifier.ModifierDAO;
import org.sing_group.piba.domain.entities.modifier.Modifier;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultModifierDAO implements ModifierDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, Modifier> dh;

  public DefaultModifierDAO() {
    super();
  }

  public DefaultModifierDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, Modifier.class, this.em);
  }

  @Override
  public Stream<Modifier> getModifiers() {
    return this.dh.list().stream();
  }

}
