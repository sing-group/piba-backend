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

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.mapper.spi.video.VideoMapper;
import org.sing_group.piba.rest.entity.video.VideoData;
import org.sing_group.piba.rest.entity.video.VideoEditionData;
import org.sing_group.piba.rest.entity.video.VideoSource;
import org.sing_group.piba.rest.resource.exploration.DefaultExplorationResource;
import org.sing_group.piba.rest.resource.video.DefaultStreamResource;
import org.sing_group.piba.service.spi.exploration.ExplorationService;

@Default
public class DefaultVideoMapper implements VideoMapper {

  private UriInfo requestURI;

  @Inject
  private ExplorationService explorationService;

  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public VideoData toVideoData(Video video) {
    return new VideoData(
      video.getId(), video.getTitle(), video.getObservations(),
      !video.isProcessing() ? videoURLs(video) : emptyList(), video.isProcessing(),
      UuidAndUri.fromEntity(requestURI, video.getExploration(), DefaultExplorationResource.class)
    );
  }

  private List<VideoSource> videoURLs(Video video) {
    return Stream.of("mp4", "ogg")
      .map(
        format -> new VideoSource(
          "video/" + format, requestURI.getBaseUriBuilder().build() + UriBuilder.fromResource(
            DefaultStreamResource.class
          ).path(video.getId()).queryParam("format", format).build().toString()
        )
      )
      .collect(Collectors.toList());
  }

  @Override
  public void assignVideoEditionData(Video video, VideoEditionData videoEditionData) {
    video.setTitle(videoEditionData.getTitle());
    video.setObservations(videoEditionData.getObservations());
    video.setProcessing(videoEditionData.isProcessing());
    video.setExploration(
      videoEditionData.getExploration() == null ? null
        : this.explorationService.getExploration(videoEditionData.getExploration())
    );
  }

}
