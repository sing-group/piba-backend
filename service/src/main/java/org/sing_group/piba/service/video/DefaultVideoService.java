/*-
 * #%L
 * Service
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Florentino Fdez-Riverola, Alba Nogueira Rodríguez
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package org.sing_group.piba.service.video;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.video.VideoDAO;
import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.service.entity.video.VideoUploadData;
import org.sing_group.piba.service.spi.exploration.ExplorationService;
import org.sing_group.piba.service.spi.storage.FileStorage;
import org.sing_group.piba.service.spi.video.VideoConversionService;
import org.sing_group.piba.service.spi.video.VideoService;

@Stateless
@PermitAll
public class DefaultVideoService implements VideoService {
  @Inject
  private VideoDAO videoDao;

  @Inject
  private FileStorage videoStorage;

  @Inject
  private VideoConversionService conversionService;

  @Inject
  private ExplorationService explorationService;

  @Override
  public Stream<Video> getVideos() {
    return videoDao.getVideos();
  }

  @Override
  public Video getVideo(String id) {
    return videoDao.getVideo(id);
  }

  public void onConversionEvent(@Observes VideoConversionTask task) {
    // conversion finished
    try {
      videoStorage.store(task.getId() + "." + task.getDestinationFormat(), new FileInputStream(task.getOutput()));
      task.getOutput().delete();

      if (task.getStatus() == VideoConversionTask.ConversionTaskStatus.FINISHED_SUCCESS) {
        Video video = videoDao.getVideo(task.getId());
        video.setProcessing(false);
        task.getInput().delete();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Video create(VideoUploadData data) {
    try {
      Exploration exploration = this.explorationService.getExploration(data.getExploration_id());

      Video video = new Video();
      video.setObservations(data.getObservations());
      video.setTitle(data.getTitle());
      video.setProcessing(true);
      video.setExploration(exploration);
      video = videoDao.create(video);

      try (FileInputStream fis = new FileInputStream(data.getVideoData())) {
        videoStorage.store(video.getId() + ".mp4", fis);
      }

      // asynchronous conversion and storage
      File oggFile = File.createTempFile("piba_converted_video_", ".ogg");
      conversionService
        .convertVideo(new VideoConversionTask(video.getId(), "mp4", "ogg", data.getVideoData(), oggFile));

      return video;

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(Video video) {
    if (video.isProcessing()) {
      throw new IllegalArgumentException("Can not be deleted until the conversion is completed");
    }
    this.videoDao.delete(video);
    this.videoStorage.delete(video.getId());
  }

  @Override
  public Video edit(Video video) {
    return this.videoDao.edit(video);
  }
}
