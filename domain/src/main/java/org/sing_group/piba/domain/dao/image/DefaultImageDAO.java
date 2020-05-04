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

import static java.util.function.UnaryOperator.identity;

import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.image.ImageDAO;
import org.sing_group.piba.domain.entities.image.Gallery;
import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.image.ImageFilter;
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
    return this.getPolypLocationIfPresent(image)
      .orElseThrow(() -> new IllegalArgumentException("No location for image: " + image.getId()));
  }

  @Override
  public Optional<PolypLocation> getPolypLocationIfPresent(Image image) {
    return this.dhPolypLocation.getBy("image", image);
  }

  @Override
  public void delete(Image image) {
    image.setRemoved(true);
    this.dh.update(image);
  }

  @Override
  public void deletePolypLocation(Image image) {
    this.getPolypLocation(image).setImage(null);
    this.dh.update(image);
  }

  @Override
  public Stream<Image> listImagesBy(Gallery gallery, Integer page, Integer pageSize, ImageFilter filter) {
    return this.listImagesBy(gallery, page, pageSize, filter, Image.class);
  }

  @Override
  public int countImagesIn(Gallery gallery, ImageFilter filter) {
    final UnaryOperator<String> transformSelect = image -> "COUNT(" + image + ")";
    final TypedQuery<Long> query = this.em.createQuery(buildListImagesBySql(filter, transformSelect, true), Long.class)
      .setParameter("gallery", gallery);
    
    return query.getSingleResult().intValue();
  }

  @Override
  public Stream<String> listImagesIdentifiersBy(Gallery gallery, Integer page, Integer pageSize, ImageFilter filter) {
    return this.listImagesBy(gallery, page, pageSize, filter, String.class);
  }
  
  private static String buildListImagesBySql(ImageFilter filter, UnaryOperator<String> transformSelect, boolean sort) {
    String query;
    switch(filter) {
      case ALL:
        query = "SELECT " + transformSelect.apply("i")
          + " FROM Image i WHERE i.gallery=:gallery AND i.isRemoved=false";
        
        if (sort) {
          query += " ORDER BY i.creationDate DESC, i.video.id, i.numFrame";
        }
        
        return query;
      case LOCATED:
        query = "SELECT " + transformSelect.apply("pl.image")
          + " FROM PolypLocation pl WHERE pl.image.gallery=:gallery AND pl.image.isRemoved=false";
        
        if (sort) {
          query += " ORDER BY pl.image.creationDate DESC, pl.image.video.id, pl.image.numFrame";
        }
        
        return query;
      case UNLOCATED:
        query = "SELECT " + transformSelect.apply("i")
          + " FROM Image i WHERE i.gallery=:gallery AND i.isRemoved=false AND NOT EXISTS (SELECT pl FROM PolypLocation pl WHERE pl.image = i)";
        
        if (sort) {
          query += " ORDER BY i.creationDate DESC, i.video.id, i.numFrame";
        }
        
        return query;
      case UNLOCATED_WITH_POLYP:
        query = "SELECT " + transformSelect.apply("i")
        + " FROM Image i WHERE i.gallery=:gallery AND i.isRemoved=false AND i.polyp IS NOT NULL AND NOT EXISTS (SELECT pl FROM PolypLocation pl WHERE pl.image = i)";
        
        if (sort) {
          query += " ORDER BY i.creationDate DESC, i.video.id, i.numFrame";
        }
        
        return query;
      case WITH_POLYP:
        query = "SELECT " + transformSelect.apply("i")
        + " FROM Image i WHERE i.gallery=:gallery AND i.isRemoved=false AND i.polyp IS NOT NULL";
        
        if (sort) {
          query += " ORDER BY i.creationDate DESC, i.video.id, i.numFrame";
        }
        
        return query;
      default:
        throw new IllegalArgumentException("Image filter not supported: " + filter);
        
    }
  }

  private <T> Stream<T> listImagesBy(Gallery gallery, Integer page, Integer pageSize, ImageFilter filter, Class<T> clazz) {
    if (!clazz.equals(String.class) && !clazz.equals(Image.class)) {
      throw new IllegalArgumentException("only String or Image classes are allowed");
    }
    
    final boolean onlyIds = clazz.equals(String.class);
    final UnaryOperator<String> transformSelect = onlyIds
      ? image -> image + ".id" : identity();

    final TypedQuery<T> query = this.em.createQuery(buildListImagesBySql(filter, transformSelect, true), clazz)
      .setParameter("gallery", gallery);
    
    if (page != null && pageSize != null) {
      return query.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList().stream();
    } else {
      return query.getResultList().stream();
    }
  }

  @Override
  public Stream<String> listImageObservationsToRemoveBy(String observationToRemoveStartsWith) {
    return this.em.createQuery(
      "SELECT DISTINCT i.observationToRemove FROM Image i WHERE i.observationToRemove LIKE :observationToRemoveStartsWith",
      String.class
    ).setParameter("observationToRemoveStartsWith", observationToRemoveStartsWith + "%").getResultList().stream();
  }

}
