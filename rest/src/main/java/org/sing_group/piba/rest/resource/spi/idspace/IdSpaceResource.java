package org.sing_group.piba.rest.resource.spi.idspace;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.piba.rest.entity.idspace.IdSpaceEditionData;

@Local
public interface IdSpaceResource {
  public Response get(String id);

  public Response getIDSpaces();

  public Response create(IdSpaceEditionData idSpaceEditionData);

  public Response edit(IdSpaceEditionData idSpaceEditionData);

  public Response delete(String id);
}
