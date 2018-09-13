package org.sing_group.piba.service.spi.user;

import javax.ejb.Local;

import org.sing_group.piba.domain.entities.user.User;

@Local
public interface UserService {
  public User getCurrentUser();
}
