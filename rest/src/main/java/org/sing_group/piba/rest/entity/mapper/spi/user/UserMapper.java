package org.sing_group.piba.rest.entity.mapper.spi.user;

import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.rest.entity.user.UserData;

public interface UserMapper {
  public UserData toUserData(User user);
}
