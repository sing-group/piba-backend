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

package org.sing_group.piba.service.spi.video;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.service.entity.video.VideoUploadData;
import org.sing_group.piba.service.video.VideoConversionTask;

@Local
public interface VideoService {
  public Video getVideo(String id);

  public boolean existsVideo(String id);
  
  public Stream<Video> getVideos();

  public Video create(VideoUploadData data);

  public void onConversionEvent(VideoConversionTask task);

  public Video edit(Video video);

  public void delete(Video video);
}
