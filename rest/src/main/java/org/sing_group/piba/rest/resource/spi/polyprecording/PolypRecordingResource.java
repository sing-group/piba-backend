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
package org.sing_group.piba.rest.resource.spi.polyprecording;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.piba.rest.entity.polyprecording.PolypRecordingEditionData;

@Local
public interface PolypRecordingResource {
  public Response getPolypResourceByVideo(String video_id);

  public Response getPolypResourceByPolyp(String polyp_id);

  public Response create(PolypRecordingEditionData polypRecordingEditionData);
  
  public Response edit(int id, PolypRecordingEditionData polypRecordingEditionData);

  public Response delete(int id);

  public Response editAll(PolypRecordingEditionData[] polypRecordingsEditionData);

}
