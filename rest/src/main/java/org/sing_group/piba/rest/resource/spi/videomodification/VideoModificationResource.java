package org.sing_group.piba.rest.resource.spi.videomodification;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.piba.rest.entity.videomodification.VideoModificationEditionData;

@Local
public interface VideoModificationResource {
  public Response create(VideoModificationEditionData videoModificationEditionData);

  public Response getVideoModification(String video_id);

  public Response delete(int id);

}
