package org.sing_group.piba.service.spi.idspace;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.piba.domain.entities.idspace.IdSpace;

@Local
public interface IdSpaceService {
  public IdSpace get(String id);

  public Stream<IdSpace> getIDSpaces();

  public IdSpace create(IdSpace idSpace);

  public IdSpace edit(IdSpace idSpace);

}
