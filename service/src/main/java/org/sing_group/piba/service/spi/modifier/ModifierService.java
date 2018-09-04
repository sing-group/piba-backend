package org.sing_group.piba.service.spi.modifier;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.piba.domain.entities.modifier.Modifier;

@Local
public interface ModifierService {
  public Stream<Modifier> getModifiers();

}
