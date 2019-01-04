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
package org.sing_group.piba.domain.dao.image;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.image.ImageDAO;
import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.PolypLocation;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultImageDAO implements ImageDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, Image> dh;
  protected DAOHelper<String, PolypLocation> dhPolypLocation;

  public DefaultImageDAO() {
    super();
  }

  public DefaultImageDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, Image.class, this.em);
    this.dhPolypLocation = DAOHelper.of(String.class, PolypLocation.class, this.em);
  }

  @Override
  public Image create(Image image) {
    return this.dh.persist(image);
  }

  @Override
  public boolean existsImage(String id) {
    return this.dh.get(id).isPresent();
  }

  @Override
  public Image get(String id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown image: " + id));
  }

  @Override
  public PolypLocation create(PolypLocation polypLocation) {
    return this.dhPolypLocation.persist(polypLocation);
  }

  @Override
  public PolypLocation getPolypLocation(Image image) {
    return this.dhPolypLocation.getBy("image", image)
      .orElseThrow(() -> new IllegalArgumentException("No location for image: " + image.getId()));
  }

  @Override
  public void delete(Image image) {
    image.setRemoved(true);
    this.dh.update(image);
  }
}
