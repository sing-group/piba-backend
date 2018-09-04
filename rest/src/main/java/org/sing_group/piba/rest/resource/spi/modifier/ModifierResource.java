package org.sing_group.piba.rest.resource.spi.modifier;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

@Local
public interface ModifierResource {
  public Response getModifiers();

}
