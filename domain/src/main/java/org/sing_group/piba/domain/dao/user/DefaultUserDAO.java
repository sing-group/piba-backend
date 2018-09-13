package org.sing_group.piba.domain.dao.user;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.user.UserDAO;
import org.sing_group.piba.domain.entities.user.User;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultUserDAO implements UserDAO {

  @PersistenceContext
  private EntityManager em;

  private DAOHelper<String, User> dh;

  DefaultUserDAO() {}

  public DefaultUserDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, User.class, this.em);
  }

  @Override
  public User get(String login) {
    return dh.get(login)
      .orElseThrow(() -> new IllegalArgumentException("Unknown user: " + login));
  }

}
