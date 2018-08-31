package org.sing_group.piba.rest.entity.mapper.idspace;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.idspace.IdSpace;
import org.sing_group.piba.rest.entity.idspace.IdSpaceData;
import org.sing_group.piba.rest.entity.mapper.spi.idspace.IdSpaceMapper;

@Default
public class DefaultIdSpaceMapper implements IdSpaceMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public IdSpaceData toIDSpaceData(IdSpace idSpace) {
    return new IdSpaceData(idSpace.getId(), idSpace.getName());
  }

}
