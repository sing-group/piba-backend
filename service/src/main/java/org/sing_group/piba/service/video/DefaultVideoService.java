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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.video.VideoDAO;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.service.entity.video.VideoUploadData;
import org.sing_group.piba.service.spi.storage.FileStorage;
import org.sing_group.piba.service.spi.video.VideoService;

@Stateless
@PermitAll
public class DefaultVideoService implements VideoService {
  @Inject
  private VideoDAO videoDao;

  @Inject
  private FileStorage videoStorage;

  @Override
  public Stream<Video> getVideos() {
    return videoDao.getVideos();
  }

  @Override
  public Video getVideo(String id) {
    return videoDao.getVideo(id);
  }

  @Override
  public Video create(VideoUploadData data) {
    try {
      Video video = new Video();
      videoStorage.store(video.getId() + ".mp4", new FileInputStream(data.getVideoData()));
      video.setObservations(data.getObservations());
      video.setTitle(data.getTitle());
      return videoDao.create(video);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
