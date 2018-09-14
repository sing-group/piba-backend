package org.sing_group.piba.domain.entities.user;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.requirePattern;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

@Entity
@Table(name = "user")
public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(length = 100, nullable = false, unique = true)
  private String login;
  @Column(length = 32, nullable = false)
  private String password;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  User() {}

  public User(String login, String password, Role role) {
    setLogin(login);
    setPassword(password);
    setRole(role);
  }

  public String getLogin() {
    return login;
  }

  void setLogin(String login) {
    this.login =
      requirePattern(
        login, "[a-zA-ZñÑ0-9_]{1,100}",
        "'login' can only contain letters, numbers or underscore and should have a length between 1 and 100"
      );
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    requireNonNull(password, "password can't be null");
    if (password.length() < 6)
      throw new IllegalArgumentException("password can't be shorter than 6");
    try {
      final MessageDigest digester = MessageDigest.getInstance("MD5");
      final HexBinaryAdapter adapter = new HexBinaryAdapter();

      this.password = adapter.marshal(digester.digest(password.getBytes()));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("MD5 algorithm not found", e);
    }
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    requireNonNull(role, "role can't be null");
    this.role = role;
  }

}