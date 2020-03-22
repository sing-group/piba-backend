/*-
 * #%L
 * Service
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
package org.sing_group.piba.service.user;

import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.sing_group.piba.domain.dao.spi.user.UserDAO;
import org.sing_group.piba.domain.entities.passwordrecovery.PasswordRecovery;
import org.sing_group.piba.domain.entities.user.LoginOrEmail;
import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.service.spi.user.UserService;

@Stateless
@PermitAll
public class DefaultUserService implements UserService {

  private static final String FRONTEND_PATH = "java:global/piba/frontend/url";

  @Resource(name = FRONTEND_PATH)
  private String frontendURL;

  @Resource(name = "java:/piba/mail")
  private Session session;

  @Inject
  private UserDAO userDAO;

  @Resource
  private SessionContext context;

  @Override
  public User getCurrentUser() {
    return userDAO.get(this.context.getCallerPrincipal().getName());
  }

  @Override
  public User get(String id) {
    return userDAO.get(id);
  }

  @Override
  public User create(User user) {
    return userDAO.create(user);
  }

  @Override
  public User edit(User user) {
    return userDAO.edit(user);
  }

  @Override
  public void delete(User user) {
    userDAO.delete(user);
  }

  @Override
  public Stream<User> listUsers() {
    return userDAO.listUsers();
  }

  @Override
  public void recoverPassword(LoginOrEmail loginOrEmail) {
    sendEmail(userDAO.createPasswordRecovery(loginOrEmail));
  }

  private void sendEmail(PasswordRecovery passwordRecovery) {
    try {
      String email = userDAO.get(passwordRecovery.getLogin()).getEmail();
      
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("piba@sing-group.org"));
      message.setRecipients(
        Message.RecipientType.TO, InternetAddress.parse(email)
      );
      message.setSubject("PIBA Password Recovery");
      message.setText(String.format(
        "Hi %s,\n\n" +
        "If you forgot your password, please click on the next URL and to change it: \n" +
        "%sloginrecovery?uuid=%s.\n" +
        "The link will be active for the next 24 hours.\n\n" +
        "Sincerely,\n\n\tThe PIBA team",
        passwordRecovery.getLogin(),  frontendURL, passwordRecovery.getUuid()
      ));

      Transport.send(message);
    } catch (MessagingException ex) {
      throw new RuntimeException("Email could not be sent.", ex);
    }
  }

  @Override
  public void updatePasswordRecovery(PasswordRecovery passwordRecovery) {
    userDAO.updatePasswordRecovery(passwordRecovery);
  }

}
