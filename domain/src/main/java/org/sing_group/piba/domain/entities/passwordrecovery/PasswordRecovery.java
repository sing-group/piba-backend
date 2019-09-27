/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato,
 *      Florentino Fdez-Riverola, Alba Nogueira Rodríguez
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
package org.sing_group.piba.domain.entities.passwordrecovery;

import static org.sing_group.fluent.checker.Checks.requirePattern;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "password_recovery")
public class PasswordRecovery {

  @Id
  @Column(length = 100, nullable = false, unique = true)
  private String login;
  @Column(length = 255, nullable = false)
  private String uuid;
  @Column(length = 32, nullable = false)
  private Date date;
  
  @Transient
  private String password;
  
  PasswordRecovery() {}

  public PasswordRecovery(String password, String uuid) {
    setPassword(password);
    setUuid(uuid);
  }
  
  public PasswordRecovery(String login) {
    setLogin(login);
    setUuid();
    setDate(date);
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

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid; 
  }
  
  public void setUuid() {
    this.uuid = UUID.randomUUID().toString().replace("-", ""); 
  }
  
  public Date getDate() {
    return date;
  }
  
  public void setDate(Date date) {
    this.date = new Date();
  }
  
  public void setPassword(String password) {
    this.password=password;
  }
  
  public String getPassword() {
    return this.password;
  }
}
