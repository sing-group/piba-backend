package org.sing_group.piba.domain.dao.spi.user;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.user.User;

public interface UserDAO {
  public User get(String login);

  public User create(User user);

  public Stream<User> getUsers();

}
