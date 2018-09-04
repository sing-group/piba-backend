package org.sing_group.piba.rest.entity.mapper.spi.modifier;

import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.rest.entity.modifier.ModifierData;

public interface ModifierMapper {

  public void setRequestURI(UriInfo requestURI);

  public ModifierData toModifierData(Modifier modifier);

}
