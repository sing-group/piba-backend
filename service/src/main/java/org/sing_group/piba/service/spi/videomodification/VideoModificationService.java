package org.sing_group.piba.service.spi.videomodification;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.domain.entities.videomodification.VideoModification;

@Local
public interface VideoModificationService {
  public VideoModification create(VideoModification videoModification);

  public Stream<VideoModification> getVideoModification(Video video);

  public void delete(VideoModification videoModification);
  
  public VideoModification get(int id);
}
