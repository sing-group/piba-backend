package org.sing_group.piba.rest.entity.mapper.spi.videomodification;

import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.videomodification.VideoModification;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationData;

public interface VideoModificationMapper {

  public void setRequestURI(UriInfo requestURI);

  public VideoModificationData toVideoModificationData(VideoModification videoModification);
}
