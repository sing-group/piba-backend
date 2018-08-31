package org.sing_group.piba.rest.entity.mapper.spi.idspace;

import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.idspace.IdSpace;
import org.sing_group.piba.rest.entity.idspace.IdSpaceData;

public interface IdSpaceMapper {
  public void setRequestURI(UriInfo requestURI);

  public IdSpaceData toIDSpaceData(IdSpace idSpace);
}
