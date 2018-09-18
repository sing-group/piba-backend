package org.sing_group.piba.domain.dao.spi.idspace;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.idspace.IdSpace;

public interface IdSpaceDAO {

  public IdSpace get(String id);

  public Stream<IdSpace> getIDSpaces();

  public IdSpace create(IdSpace idSpace);

  public IdSpace edit(IdSpace idSpace);
}
