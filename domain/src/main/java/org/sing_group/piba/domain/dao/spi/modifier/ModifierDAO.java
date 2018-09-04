package org.sing_group.piba.domain.dao.spi.modifier;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.modifier.Modifier;

public interface ModifierDAO {

  public Stream<Modifier> getModifiers();

}
