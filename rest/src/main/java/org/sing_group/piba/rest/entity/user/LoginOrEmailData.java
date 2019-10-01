
/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato,
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
package org.sing_group.piba.rest.entity.user;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.domain.entities.user.LoginOrEmail;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "login-or-email-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "login-or-email-data", description = "Information of user that request a password recovery.")
public class LoginOrEmailData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "loginOrEmail", required = true)
  private String loginOrEmail;

  LoginOrEmailData() {}

  public LoginOrEmailData(String loginOrEmail) {
    this.loginOrEmail = loginOrEmail;
  }

  public String getLoginOrEmail() {
    return loginOrEmail;
  }

  public boolean isEmail() {
    return LoginOrEmail.EMAIL_PATTERN.matcher(this.loginOrEmail).matches();
  }

}
