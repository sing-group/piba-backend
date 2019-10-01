/*-
 * #%L
 * REST
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
package org.sing_group.piba.rest.entity.mapper.spi;

import org.sing_group.piba.domain.entities.passwordrecovery.PasswordRecovery;
import org.sing_group.piba.domain.entities.user.LoginOrEmail;
import org.sing_group.piba.rest.entity.user.LoginOrEmailData;
import org.sing_group.piba.rest.entity.user.PasswordRecoveryData;

public interface PasswordRecoveryMapper {
  
  public PasswordRecovery toPasswordRecovery(PasswordRecoveryData passwordRecovery);

  public LoginOrEmail toLoginOrEmail(LoginOrEmailData loginOrEmail);

}
