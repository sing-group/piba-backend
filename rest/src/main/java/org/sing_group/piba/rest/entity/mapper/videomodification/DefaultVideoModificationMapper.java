package org.sing_group.piba.rest.entity.mapper.videomodification;

import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.videomodification.VideoModification;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.mapper.spi.videomodification.VideoModificationMapper;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationData;
import org.sing_group.piba.rest.resource.modifier.DefaultModifierResource;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;

public class DefaultVideoModificationMapper implements VideoModificationMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public VideoModificationData toVideoModificationData(VideoModification videoModification) {
    return new VideoModificationData(
      videoModification.getId(),
      UuidAndUri.fromEntity(requestURI, videoModification.getVideo(), DefaultVideoResource.class),
      UuidAndUri.fromEntity(requestURI, videoModification.getModifier(), DefaultModifierResource.class),
      videoModification.getStart(), videoModification.getEnd()
    );
  }

}
