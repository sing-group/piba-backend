package org.sing_group.piba.rest.entity.mapper.modifier;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.rest.entity.mapper.spi.modifier.ModifierMapper;
import org.sing_group.piba.rest.entity.modifier.ModifierData;

@Default
public class DefaultModifierMapper implements ModifierMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public ModifierData toModifierData(Modifier modifier) {
    return new ModifierData(modifier.getId(), modifier.getName());
  }

}
