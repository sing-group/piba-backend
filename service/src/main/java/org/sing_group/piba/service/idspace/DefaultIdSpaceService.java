package org.sing_group.piba.service.idspace;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.idspace.IdSpaceDAO;
import org.sing_group.piba.domain.entities.idspace.IdSpace;
import org.sing_group.piba.service.spi.idspace.IdSpaceService;
import org.sing_group.piba.service.spi.patient.PatientService;

@Stateless
@PermitAll
public class DefaultIdSpaceService implements IdSpaceService {

  @Inject
  private IdSpaceDAO idSpaceDAO;

  @Inject
  private PatientService patientService;

  @Override
  public IdSpace get(String id) {
    return idSpaceDAO.get(id);
  }

  @Override
  public Stream<IdSpace> getIDSpaces() {
    return idSpaceDAO.getIDSpaces();
  }

  @Override
  public IdSpace create(IdSpace idSpace) {
    return idSpaceDAO.create(idSpace);
  }

  @Override
  public IdSpace edit(IdSpace idSpace) {
    return idSpaceDAO.edit(idSpace);
  }

  @Override
  public void delete(IdSpace idSpace) {
    if (this.patientService.getPatientsBy(idSpace).count() > 0) {
      throw new IllegalArgumentException("Can not remove space with patients");
    }
    idSpaceDAO.delete(idSpace);
  }

}
