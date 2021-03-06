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
import org.sing_group.piba.domain.entities.polyp.Polyp;

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
    final long count = this.em.createQuery(
      "SELECT COUNT(i) FROM Image i WHERE i.numFrame = :numFrame AND i.gallery = :gallery AND i.video = :video AND i.isRemoved = false",
      Long.class
    )
      .setParameter("numFrame", image.getNumFrame())
      .setParameter("gallery", image.getGallery())
      .setParameter("video", image.getVideo())
    .getSingleResult();
    
    if (count > 0) {
      throw new IllegalArgumentException("An image already exists for the same frame.");
    }
    
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
  public PolypLocation createPolypLocation(PolypLocation polypLocation) {
    return this.dhPolypLocation.persist(polypLocation);
  }
  
  @Override
  public PolypLocation modifyPolypLocation(PolypLocation polypLocation) {
    return this.dhPolypLocation.update(polypLocation);
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
  public Stream<Image> listImagesByGallery(Gallery gallery, Integer page, Integer pageSize, ImageFilter filter) {
    return this.listImagesByGallery(gallery, page, pageSize, filter, Image.class);
  }

  @Override
  public int countImagesInGallery(Gallery gallery, ImageFilter filter) {
    final UnaryOperator<String> transformSelect = image -> "COUNT(" + image + ")";
    final TypedQuery<Long> query = this.em.createQuery(buildListImagesBySql("gallery", "gallery", filter, transformSelect, true), Long.class)
      .setParameter("gallery", gallery);
    
    return query.getSingleResult().intValue();
  }

  @Override
  public Stream<String> listImagesIdentifiersByGallery(Gallery gallery, Integer page, Integer pageSize, ImageFilter filter) {
    return this.listImagesByGallery(gallery, page, pageSize, filter, String.class);
  }
  
  @Override
  public Stream<Image> listImagesByPolyp(Polyp polyp, Integer page, Integer pageSize, ImageFilter filter) {
    final TypedQuery<Image> query = this.em.createQuery(buildListImagesBySql(
      "polyp", "polyp", filter, identity(), true), Image.class
    ).setParameter("polyp", polyp);
    
    if (page != null && pageSize != null) {
      return query.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList().stream();
    } else {
      return query.getResultList().stream();
    }
  }
  
  @Override
  public int countImagesInPolyp(Polyp polyp, ImageFilter filter) {
    final UnaryOperator<String> transformSelect = image -> "COUNT(" + image + ")";
    final TypedQuery<Long> query = this.em.createQuery(buildListImagesBySql("polyp", "polyp", filter, transformSelect, true), Long.class)
      .setParameter("polyp", polyp);
    
    return query.getSingleResult().intValue();
  }
  
  @Override
  public Stream<Image> listImagesByPolypAndGallery(
    Polyp polyp, Gallery gallery, Integer page, Integer pageSize, ImageFilter filter
  ) {
    final TypedQuery<Image> query = this.em.createQuery(buildListImagesBySql(
      new String[] { "polyp", "gallery" }, new String[] { "polyp", "gallery" }, filter, identity(), true), Image.class
    )
      .setParameter("polyp", polyp)
      .setParameter("gallery", gallery);
    
    if (page != null && pageSize != null) {
      return query.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList().stream();
    } else {
      return query.getResultList().stream();
    }
  }
  
  @Override
  public int countImagesByPolypAndGallery(Polyp polyp, Gallery gallery, ImageFilter filter) {
    final UnaryOperator<String> transformSelect = image -> "COUNT(" + image + ")";
    final TypedQuery<Long> query = this.em.createQuery(buildListImagesBySql(
      new String[] { "polyp", "gallery" }, new String[] { "polyp", "gallery" }, filter, transformSelect, true), Long.class
    )
      .setParameter("polyp", polyp)
      .setParameter("gallery", gallery);
    
    return query.getSingleResult().intValue();
  }
  
  
  private static String buildListImagesBySql(
    String field, String alias,
    ImageFilter filter, UnaryOperator<String> transformSelect, boolean sort
  ) {
    return buildListImagesBySql(
      new String[] { field }, new String[] { alias }, filter, transformSelect, sort
    );
  }
  
  private static String buildListImagesBySql(
    String[] fields, String[] aliases,
    ImageFilter filter, UnaryOperator<String> transformSelect, boolean sort
  ) {
    if (fields.length != aliases.length) {
      throw new IllegalArgumentException("fields and aliases must have the same length");
    } else if (fields.length == 0) {
      throw new IllegalArgumentException("fields can't be empty");
    } else if (aliases.length == 0) {
      throw new IllegalArgumentException("fields can't be empty");
    }
    
    final UnaryOperator<String> transformConditions = image -> {
      final StringBuilder conditions = new StringBuilder();
      for (int i = 0; i < fields.length; i++) {
        if (conditions.length() >  0) {
          conditions.append(" AND ");
        }
        conditions.append(image + "." + fields[i] + "=:" + aliases[i]);
      }
      return conditions.toString();
    };
    
    final StringBuilder query = new StringBuilder(String.format("SELECT %s FROM Image i WHERE %s AND i.isRemoved=false",
      transformSelect.apply("i"), transformConditions.apply("i")
    ));
    
    switch(filter) {
      case ALL:
        break;
      case WITHOUT_POLYP:
        query.append(" AND i.polyp IS NULL");
        
        break;
      case WITH_POLYP:
        query.append(" AND i.polyp IS NOT NULL");
        
        break;
      case WITHOUT_LOCATION:
        query.append(" AND NOT EXISTS (SELECT pl FROM PolypLocation pl WHERE pl.image = i)");
        
        break;
      case WITH_LOCATION:
        query.append(" AND EXISTS (SELECT pl FROM PolypLocation pl WHERE pl.image = i)");
        
        break;
      case WITHOUT_POLYP_AND_LOCATION:
        query.append(" AND i.polyp IS NULL AND NOT EXISTS (SELECT pl FROM PolypLocation pl WHERE pl.image = i)");
        
        break;
      case WITHOUT_POLYP_AND_WITH_LOCATION:
        query.append(" AND i.polyp IS NULL AND EXISTS (SELECT pl FROM PolypLocation pl WHERE pl.image = i)");
        
        break;
      case WITH_POLYP_AND_WITHOUT_LOCATION:
        query.append(" AND i.polyp IS NOT NULL AND NOT EXISTS (SELECT pl FROM PolypLocation pl WHERE pl.image = i)");
        
        break;
      case WITH_POLYP_AND_LOCATION:
        query.append(" AND i.polyp IS NOT NULL AND EXISTS (SELECT pl FROM PolypLocation pl WHERE pl.image = i)");
        
        break;
      default:
        throw new IllegalArgumentException("Image filter not supported: " + filter);
    }
    
    if (sort) {
      query.append(" ORDER BY i.creationDate DESC, i.video.id, i.numFrame");
    }
    
    return query.toString();
  }

  private <T> Stream<T> listImagesByGallery(Gallery gallery, Integer page, Integer pageSize, ImageFilter filter, Class<T> clazz) {
    if (!clazz.equals(String.class) && !clazz.equals(Image.class)) {
      throw new IllegalArgumentException("only String or Image classes are allowed");
    }
    
    final boolean onlyIds = clazz.equals(String.class);
    final UnaryOperator<String> transformSelect = onlyIds
      ? image -> image + ".id" : identity();

    final TypedQuery<T> query = this.em.createQuery(buildListImagesBySql("gallery", "gallery", filter, transformSelect, true), clazz)
      .setParameter("gallery", gallery);
    
    if (page != null && pageSize != null) {
      return query.setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList().stream();
    } else {
      return query.getResultList().stream();
    }
  }

  @Override
  public Stream<String> listImageObservationsToRemoveBy(String observationToRemoveStartsWith) {
    String query = "SELECT DISTINCT i.observationToRemove FROM Image i WHERE i.observationToRemove IS NOT NULL";
    
    if (observationToRemoveStartsWith != null && !observationToRemoveStartsWith.trim().isEmpty()) {
      query += " AND i.observationToRemove LIKE :observationToRemoveStartsWith ORDER BY i.observationToRemove ASC";
      
      return this.em.createQuery(query, String.class)
        .setParameter("observationToRemoveStartsWith", observationToRemoveStartsWith + "%")
      .getResultList().stream();
    } else {
      query += " ORDER BY i.observationToRemove ASC";
      
      return this.em.createQuery(query, String.class).getResultList().stream();
    }
  }
}
