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
package org.sing_group.piba.service.modifier;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.modifier.ModifierDAO;
import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.service.spi.modifier.ModifierService;
import org.sing_group.piba.service.spi.videomodification.VideoModificationService;

@Stateless
@PermitAll
public class DefaultModifierService implements ModifierService {

  @Inject
  private ModifierDAO modifierDAO;

  @Inject
  private VideoModificationService videoModificationService;

  @Override
  public Stream<Modifier> listModifiers() {
    return modifierDAO.listModifiers();
  }

  @Override
  public Modifier get(String id) {
    return modifierDAO.get(id);
  }

  @Override
  public Modifier create(Modifier modifier) {
    return modifierDAO.create(modifier);
  }

  @Override
  public Modifier edit(Modifier modifier) {
    return modifierDAO.edit(modifier);
  }

  @Override
  public void delete(Modifier modifier) {
    if (this.videoModificationService.getVideoModification(modifier).count() > 0) {
      throw new IllegalArgumentException("Can not remove modifier that has already been assigned.");
    }
    modifierDAO.delete(modifier);
  }

}
