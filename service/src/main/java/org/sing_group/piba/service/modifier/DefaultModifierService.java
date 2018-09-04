package org.sing_group.piba.service.modifier;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.modifier.ModifierDAO;
import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.service.spi.modifier.ModifierService;

@Stateless
@PermitAll
public class DefaultModifierService implements ModifierService {

  @Inject
  private ModifierDAO modifierDAO;

  @Override
  public Stream<Modifier> getModifiers() {
    return modifierDAO.getModifiers();
  }

}
