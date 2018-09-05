package org.sing_group.piba.service.spi.videomodification;

import javax.ejb.Local;

import org.sing_group.piba.domain.entities.videomodification.VideoModification;

@Local
public interface VideoModificationService {
  public VideoModification create(VideoModification videoModification);
}
