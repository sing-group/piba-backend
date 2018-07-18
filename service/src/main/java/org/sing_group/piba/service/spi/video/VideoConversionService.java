package org.sing_group.piba.service.spi.video;

import javax.ejb.Local;

import org.sing_group.piba.service.video.VideoConversionTask;

@Local
public interface VideoConversionService {

  public void convertVideo(VideoConversionTask task);
}
