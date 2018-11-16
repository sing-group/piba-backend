/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato,
 * 			Florentino Fdez-Riverola, Alba Nogueira Rodríguez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
  @Column(length = 120, nullable = false)
  private String email;
  @Column(length = 32, nullable = false)
  private String password;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  User() {}

  public User(String login, String email, String password, Role role) {
    setLogin(login);
    setEmail(email);
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email =
      requirePattern(
        email, "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", "invalid format email"
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
