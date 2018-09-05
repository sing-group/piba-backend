package org.sing_group.piba.service.videomodification;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.videomodification.VideoModificationDAO;
import org.sing_group.piba.domain.entities.videomodification.VideoModification;
import org.sing_group.piba.service.spi.videomodification.VideoModificationService;

@Stateless
@PermitAll
public class DefaultVideoModificationService implements VideoModificationService {

  @Inject
  private VideoModificationDAO videoModificationDAO;

  @Override
  public VideoModification create(VideoModification videoModification) {
    return videoModificationDAO.create(videoModification);
  }

}
