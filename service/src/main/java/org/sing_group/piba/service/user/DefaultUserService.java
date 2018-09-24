package org.sing_group.piba.service.user;

import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.user.UserDAO;
import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.service.spi.user.UserService;

@Stateless
@PermitAll
public class DefaultUserService implements UserService {

  @Inject
  private UserDAO userDAO;

  @Resource
  private SessionContext context;

  @Override
  public User getCurrentUser() {
    return userDAO.get(this.context.getCallerPrincipal().getName());
  }

  @Override
  public User create(User user) {
    return userDAO.create(user);
  }

  @Override
  public Stream<User> getUsers() {
    return userDAO.getUsers();
  }

}
