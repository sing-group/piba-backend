/*-
 * #%L
 * Service
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
package org.sing_group.piba.service.polyprecording;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.polyprecording.PolypRecordingDAO;
import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.service.spi.polyprecording.PolypRecordingService;

@Stateless
@PermitAll
public class DefaultPolypRecordingService implements PolypRecordingService {

  @Inject
  private PolypRecordingDAO polypRecordingDAO;

  @Override
  public Stream<PolypRecording> get(Video video) {
    return this.polypRecordingDAO.get(video);
  }

  @Override
  public PolypRecording create(PolypRecording polypRecording) {
    if(polypRecording.getPolyp().getExploration().getId() != polypRecording.getVideo().getExploration().getId()) {
      throw new IllegalArgumentException("Do not belong to the same exploration");
    }
    return this.polypRecordingDAO.create(polypRecording);
  }

}