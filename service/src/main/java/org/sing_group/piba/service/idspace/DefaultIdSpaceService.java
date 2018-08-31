package org.sing_group.piba.service.idspace;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.idspace.IdSpaceDAO;
import org.sing_group.piba.domain.entities.idspace.IdSpace;
import org.sing_group.piba.service.spi.idspace.IdSpaceService;

@Stateless
@PermitAll
public class DefaultIdSpaceService implements IdSpaceService {

  @Inject
  private IdSpaceDAO idSpaceDAO;

  @Override
  public IdSpace get(String id) {
    return idSpaceDAO.get(id);
  }

  @Override
  public Stream<IdSpace> getIDSpaces() {
    return idSpaceDAO.getIDSpaces();
  }

}
