package org.sing_group.piba.rest.entity.user;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.domain.entities.user.Role;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "User-edition-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "User-edition-data", description = "Information of a user for edition.")
public class UserEditionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "login", required = true)
  private String login;
  @XmlElement(name = "password")
  private String password;
  @XmlElement(name = "role", required = true)
  private Role role;

  UserEditionData() {}

  public UserEditionData(String login, String password, Role role) {
    this.login = login;
    this.password = password;
    this.role = role;
  }

  public String getLogin() {
    return login;
  }

  public String getPassword() {
    return password;
  }

  public Role getRole() {
    return role;
  }

}
