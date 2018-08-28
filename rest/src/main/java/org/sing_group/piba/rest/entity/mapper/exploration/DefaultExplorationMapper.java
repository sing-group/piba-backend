package org.sing_group.piba.rest.entity.mapper.exploration;

import static org.sing_group.piba.rest.entity.UuidAndUri.fromEntities;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.rest.entity.exploration.ExplorationData;
import org.sing_group.piba.rest.entity.exploration.ExplorationEditionData;
import org.sing_group.piba.rest.entity.mapper.spi.exploration.ExplorationMapper;
import org.sing_group.piba.rest.resource.polyp.DefaultPolypResource;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;

@Default
public class DefaultExplorationMapper implements ExplorationMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }
  
  @Override
  public ExplorationData toExplorationData(Exploration exploration) {
    return new ExplorationData(
      exploration.getId(), exploration.getLocation(), exploration.getDate(),
      fromEntities(requestURI, exploration.getVideos(), DefaultVideoResource.class),
      fromEntities(requestURI, exploration.getPolyps(), DefaultPolypResource.class),
      exploration.getPatient().getId()
    );
  }

  @Override
  public void assignExplorationEditData(Exploration exploration, ExplorationEditionData explorationEditionData) {
    exploration.setDate(explorationEditionData.getDate());
    exploration.setLocation(explorationEditionData.getLocation());
  }
}
