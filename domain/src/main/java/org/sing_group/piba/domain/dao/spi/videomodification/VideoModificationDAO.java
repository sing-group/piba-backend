package org.sing_group.piba.domain.dao.spi.videomodification;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.domain.entities.videomodification.VideoModification;

public interface VideoModificationDAO {

  public VideoModification create(VideoModification videoModification);

  public Stream<VideoModification> getVideoModification(Video video);

  public void delete(Video video, Modifier modifier);

}
