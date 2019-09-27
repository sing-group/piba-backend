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
package org.sing_group.piba.domain.dao.user;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.user.UserDAO;
import org.sing_group.piba.domain.entities.passwordrecovery.PasswordRecovery;
import org.sing_group.piba.domain.entities.user.User;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultUserDAO implements UserDAO {

  @PersistenceContext
  private EntityManager em;

  private DAOHelper<String, User> dh;
  private DAOHelper<String, PasswordRecovery> dhPass;

  DefaultUserDAO() {}

  public DefaultUserDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, User.class, this.em);
    this.dhPass = DAOHelper.of(String.class, PasswordRecovery.class, this.em);
  }
  
  @Override
  public User get(String login) {
    return dh.get(login)
      .orElseThrow(() -> new IllegalArgumentException("Unknown user: " + login));
  }

  @Override
  public User create(User user) {
    return this.dh.persist(user);
  }

  @Override
  public User edit(User user) {
    return this.dh.update(user);
  }

  @Override
  public void delete(User user) {
    this.dh.remove(user);
  }

  @Override
  public Stream<User> getUsers() {
    return this.dh.list().stream();
  }

  @Override
  public PasswordRecovery createPasswordRecovery(String loginOrEmail) {
    User user = getUserByLoginOrEmail(loginOrEmail);
    String login = user.getLogin();

    PasswordRecovery passRecovery;
    try {
      Date now = new Date();
      passRecovery =
        this.dhPass.get(login)
          .orElseThrow(() -> new IllegalArgumentException("Recovery not created yet."));
      if (passRecovery.getDate().toInstant()
        .plus(24, ChronoUnit.HOURS).isBefore(now.toInstant())) {
        passRecovery = this.dhPass.update(new PasswordRecovery(login));
      }
    } catch (IllegalArgumentException e) {
      passRecovery = this.dhPass.persist(new PasswordRecovery(login));
    }
    return passRecovery;

  }

  private User getUserByLoginOrEmail(final String loginOrEmail) {
    if (loginOrEmail.contains("@")) {
      return dh.getBy("email", loginOrEmail)
          //.map(User::getLogin)
          .orElseThrow(() -> new IllegalArgumentException("Unknown user: " + loginOrEmail));
    } else {
      return dh.get(loginOrEmail)
          .orElseThrow(() -> new IllegalArgumentException("Unknown user: " + loginOrEmail));
    }
  }

  @Override
  public void updatePasswordRecovery(PasswordRecovery passwordRecovery) {

    PasswordRecovery passAux;
    Date now = new Date();
    passAux =
      dhPass.getBy("uuid", passwordRecovery.getUuid())
        .orElseThrow(() -> new IllegalArgumentException("No recovery with this uuid"));
    if (passAux.getDate().toInstant().plus(24, ChronoUnit.HOURS).isAfter(now.toInstant())) {
      User user = this.get(passAux.getLogin());
      user.setPassword(passwordRecovery.getPassword());
      this.edit(user);
      this.dhPass.remove(passAux);
    } else {
      this.dhPass.remove(passAux);
      throw new IllegalArgumentException("Recovery has passed the date limit");
    }

  }
  
  @Override
  public Stream<PasswordRecovery> getAllPasswordRecovery(){
    return this.dhPass.list().stream();
  }

  @Override
  public void deletePasswordRecovery(PasswordRecovery passwordRecovery) {
    this.dhPass.remove(passwordRecovery);
    
  }

}
