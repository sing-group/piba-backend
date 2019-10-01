/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato,
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

import java.util.regex.Pattern;

public class LoginOrEmail {
  public final static String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
  public final static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

  private String loginOrEmail;

  public LoginOrEmail(String loginOrEmail) {
    this.loginOrEmail = loginOrEmail;
  }

  public String getLoginOrEmail() {
    return loginOrEmail;
  }

  public boolean isEmail() {
    return EMAIL_PATTERN.matcher(this.loginOrEmail).matches();
  }

}
