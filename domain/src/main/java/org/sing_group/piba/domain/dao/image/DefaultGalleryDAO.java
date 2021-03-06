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

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.image.GalleryDAO;
import org.sing_group.piba.domain.entities.image.Gallery;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultGalleryDAO implements GalleryDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, Gallery> dh;

  public DefaultGalleryDAO() {
    super();
  }

  public DefaultGalleryDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, Gallery.class, this.em);
  }

  @Override
  public Gallery create(Gallery gallery) {
    return this.dh.persist(gallery);
  }

  @Override
  public Gallery get(String id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown gallery: " + id));
  }

  @Override
  public Stream<Gallery> listGalleries() {
    return this.dh.list().stream();
  }

  @Override
  public Gallery edit(Gallery gallery) {
    return this.dh.update(gallery);
  }
}
