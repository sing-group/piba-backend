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

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.mapper.spi.PolypRecordingMapper;
import org.sing_group.piba.rest.entity.polyprecording.PolypRecordingData;
import org.sing_group.piba.rest.entity.polyprecording.PolypRecordingEditionData;
import org.sing_group.piba.rest.resource.polyp.DefaultPolypResource;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;
import org.sing_group.piba.service.spi.polyp.PolypService;
import org.sing_group.piba.service.spi.video.VideoService;

@Default
public class DefaultPolypRecordingMapper implements PolypRecordingMapper {

  private UriInfo requestURI;

  @Inject
  private PolypService polypService;

  @Inject
  private VideoService videoService;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public PolypRecordingData toPolypRecordingData(PolypRecording polypRecording) {
    return new PolypRecordingData(
      polypRecording.getId(),
      UuidAndUri.fromEntity(requestURI, polypRecording.getVideo(), DefaultVideoResource.class),
      UuidAndUri.fromEntity(requestURI, polypRecording.getPolyp(), DefaultPolypResource.class),
      polypRecording.getStart(), polypRecording.getEnd(), polypRecording.isConfirmed()
    );
  }

  @Override
  public void assignPolypRecordingEditionData(
    PolypRecording polypRecording, PolypRecordingEditionData polypRecordingEditicionData
  ) {
    polypRecording.setStart(polypRecordingEditicionData.getStart());
    polypRecording.setEnd(polypRecordingEditicionData.getEnd());
    polypRecording.setPolyp(this.polypService.getPolyp(polypRecordingEditicionData.getPolyp()));
    polypRecording.setVideo(this.videoService.getVideo(polypRecordingEditicionData.getVideo()));
    polypRecording.setConfirmed(polypRecordingEditicionData.isConfirmed());
  }

}
