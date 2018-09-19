package org.sing_group.piba.rest.entity.mapper.user;

import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.rest.entity.mapper.spi.user.UserMapper;
import org.sing_group.piba.rest.entity.user.UserData;

public class DefaultUserMapper implements UserMapper {

  @Override
  public UserData toUserData(User user) {
    return new UserData(user.getLogin(), user.getPassword(), user.getRole());
  }

}
