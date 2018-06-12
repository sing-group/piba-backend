/*-
 * #%L
 * REST
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

package org.sing_group.piba.rest.entity.mapper.video;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;

import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.rest.entity.mapper.spi.video.VideoMapper;
import org.sing_group.piba.rest.entity.video.VideoData;
import org.sing_group.piba.rest.entity.video.VideoSource;

@Default
public class DefaultVideoMapper implements VideoMapper {

  @Override
  public VideoData toVideoData(Video video) {

    return new VideoData(
      video.getId(), video.getTitle(), video.getObservations(),
      Stream.of("mp4", "ogg")
        .map(
          format -> new VideoSource(
            "video/" + format, "http://static.sing-group.org/polydeep/videos/sample-exploration." + format
          )
        )
        .collect(Collectors.toList())
    );
  }
}
