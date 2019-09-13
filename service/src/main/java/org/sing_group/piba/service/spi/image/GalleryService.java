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
package org.sing_group.piba.service.spi.image;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

import javax.ejb.Local;
import javax.ws.rs.core.StreamingOutput;

import org.sing_group.piba.domain.entities.image.Gallery;

@Local
public interface GalleryService {
  public Gallery create(Gallery galery);

  public Gallery get(String id);

  public Stream<Gallery> getGalleries();

  public Gallery edit(Gallery gallery);

  public StreamingOutput getGalleriesInZip(Gallery gallery, String filter, Boolean withLocation)
    throws FileNotFoundException;
}
