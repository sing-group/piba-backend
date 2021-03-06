/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato,
 * 			Florentino Fdez-Riverola, Alba Nogueira Rodríguez
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
package org.sing_group.piba.rest.entity.mapper;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.videomodification.VideoModification;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.mapper.spi.VideoModificationMapper;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationData;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationEditionData;
import org.sing_group.piba.rest.resource.modifier.DefaultModifierResource;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;
import org.sing_group.piba.service.spi.modifier.ModifierService;
import org.sing_group.piba.service.spi.video.VideoService;

public class DefaultVideoModificationMapper implements VideoModificationMapper {

  private UriInfo requestURI;

  @Inject
  private ModifierService modifierService;

  @Inject
  private VideoService videoService;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public VideoModificationData toVideoModificationData(VideoModification videoModification) {
    return new VideoModificationData(
      videoModification.getId(),
      UuidAndUri.fromEntity(requestURI, videoModification.getVideo(), DefaultVideoResource.class),
      UuidAndUri.fromEntity(requestURI, videoModification.getModifier(), DefaultModifierResource.class),
      videoModification.getStart(), videoModification.getEnd(), videoModification.isConfirmed()
    );
  }

  @Override
  public void assignVideoModificationEditionData(
    VideoModification videoModification, VideoModificationEditionData videoModificationEditionData
  ) {
    videoModification.setStart(videoModificationEditionData.getStart());
    videoModification.setEnd(videoModificationEditionData.getEnd());
    videoModification.setModifier(this.modifierService.get(videoModificationEditionData.getModifier()));
    videoModification.setVideo(this.videoService.getVideo(videoModificationEditionData.getVideo()));
    videoModification.setConfirmed(videoModificationEditionData.isConfirmed());
  }

}
