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
package org.sing_group.piba.rest.resource.spi.image;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.piba.rest.entity.RestImageUploadData;
import org.sing_group.piba.rest.entity.image.PolypLocationEditionData;

@Local
public interface ImageResource {

  public Response uploadImage(RestImageUploadData restImageUploadData);

  public Response getImage(String id);

  public Response getBytes(String id);

  public Response createPolypLocation(String id, PolypLocationEditionData polypLocationEditicionData);

  public Response getPolypLocation(String id);

  public Response delete(String id, String observationToRemove);

  public Response deletePolypLocation(String id);

}
