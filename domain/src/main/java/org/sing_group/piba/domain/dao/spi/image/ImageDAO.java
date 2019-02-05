/*-
 * #%L
 * Domain
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
package org.sing_group.piba.domain.dao.spi.image;

import java.util.stream.Stream;

import org.sing_group.piba.domain.entities.image.Gallery;
import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.PolypLocation;

public interface ImageDAO {

  public Image create(Image image);

  public boolean existsImage(String id);

  public Image get(String id);

  public PolypLocation create(PolypLocation polypLocation);

  public PolypLocation getPolypLocation(Image image);

  public void delete(Image image);

  public void deletePolypLocation(Image image);

  public Stream<Image> getImagesBy(Gallery gallery, Integer page, Integer pageSize, String format);

  public int totalImagesIn(Gallery gallery, String filter);

  public Stream<String> getImagesIdentifiersBy(Gallery gallery, Integer page, Integer pageSize, String filter);

}
