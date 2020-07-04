/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Florentino Fdez-Riverola, Alba Nogueira Rodríguez
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

package org.sing_group.piba.domain.dao.polyp;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.ListingOptions;
import org.sing_group.piba.domain.dao.ListingOptions.ListingOptionsBuilder;
import org.sing_group.piba.domain.dao.ListingOptions.SortField;
import org.sing_group.piba.domain.dao.SortDirection;
import org.sing_group.piba.domain.dao.spi.polyp.PolypDatasetDAO;
import org.sing_group.piba.domain.entities.image.Gallery;
import org.sing_group.piba.domain.entities.image.Image;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyp.PolypDataset;
import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultPolypDatasetDAO implements PolypDatasetDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, PolypDataset> dh;
  protected DAOHelper<String, Polyp> dhPolyp;
  protected DAOHelper<String, PolypRecording> dhPolypRecording;
  protected DAOHelper<String, Gallery> dhGallery;
  protected DAOHelper<String, Image> dhImage;

  public DefaultPolypDatasetDAO() {
    super();
  }

  public DefaultPolypDatasetDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, PolypDataset.class, this.em);
    this.dhPolyp = DAOHelper.of(String.class, Polyp.class, this.em);
    this.dhPolypRecording = DAOHelper.of(String.class, PolypRecording.class, em);
    this.dhGallery = DAOHelper.of(String.class, Gallery.class, em);
    this.dhImage = DAOHelper.of(String.class, Image.class, em);
  }

  @Override
  public PolypDataset getPolypDataset(String id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown polyp dataset: " + id));
  }

  @Override
  public Stream<PolypDataset> listPolypDatasets(int page, int pageSize) {
    final ListingOptions listingOptions = ListingOptions.forPage(page, pageSize
      ).sortedBy(SortField.ascending("title"));
    
    return this.dh.list(listingOptions).stream();
  }

  @Override
  public Stream<Polyp> listPolypsInDataset(String datasetId, int page, int pageSize) {
    final PolypDataset dataset = this.dh.get(datasetId)
      .orElseThrow(() -> new IllegalArgumentException("Dataset not found: " + datasetId));
    
    final ListingOptions listingOptions = ListingOptions.forPage(page, pageSize).unsorted();

    return this.dhPolyp.list(listingOptions, (cb, root) -> {
      return new Predicate[] {
        cb.isMember(dataset, root.get("polypDatasets"))
      };
    }).stream();
  }

  @Override
  public Stream<PolypRecording> listPolypRecordingsInDatasets(
    String datasetId, Integer page, Integer pageSize, SortDirection imagesSort
  ) {
    final PolypDataset dataset = this.dh.get(datasetId)
      .orElseThrow(() -> new IllegalArgumentException("Dataset not found: " + datasetId));
    
    
    if (imagesSort == null || imagesSort == SortDirection.NONE) {
      final ListingOptionsBuilder listingOptionsBuilder = (page == null || pageSize == null)
        ? ListingOptions.allResults() : ListingOptions.forPage(page, pageSize);
        
      final ListingOptions listingOptions = listingOptionsBuilder.sortedBy(SortField.ascending("creationDate"));

      return this.dhPolypRecording.list(listingOptions, (cb, root) -> new Predicate[] {
        cb.isMember(dataset, root.join("polyp").get("polypDatasets"))
      }).stream();
    } else {
      return this.listPolypRecordingsInDatasetOrderedByImages(dataset, page, pageSize, imagesSort);
    }
  }

  @SuppressWarnings("unchecked")
  private Stream<PolypRecording> listPolypRecordingsInDatasetOrderedByImages(
    PolypDataset dataset, Integer page, Integer pageSize, SortDirection imagesSort
  ) {
    // Native query is used because it is not possible to do a LEFT JOIN with conditions in JQL
    final String querySql =
      "SELECT pr.* " +
        "FROM polyprecording pr " +
        "JOIN video v ON v.id = pr.video_id " +
        "LEFT JOIN polypsindataset pid ON pr.polyp_id = pid.polyp_id " +
        "LEFT JOIN polypdataset pd ON pid.polypdataset_id = pd.id AND pd.id = :dataset " +
        "LEFT JOIN image i ON NOT i.is_removed " +
          "AND i.gallery_id = pd.defaultGallery_id " +
          "AND i.video_id = pr.video_id AND i.polyp_id = pr.polyp_id " +
          "AND i.num_frame / v.fps BETWEEN pr.start AND pr.end + 0.999 " +
        "GROUP BY pr.id " +
        "ORDER BY COUNT(DISTINCT i.id) " +
        (imagesSort == SortDirection.ASCENDING ? "ASC" : "DESC") +
        ", pr.creation_date ASC";
        
    Query query = this.em.createNativeQuery(querySql, PolypRecording.class)
      .setParameter("dataset", dataset.getId());
    
    if (page != null && pageSize != null) {
      query = query
        .setFirstResult((page - 1) * pageSize)
        .setMaxResults(pageSize);
    }
    
    return query.getResultList().stream();
  }
  
  @Override
  public int countPolypDatasets() {
    return (int) this.dh.count();
  }

  @Override
  public int countPolypsInDataset(String datasetId) {
    return this.dh.countRelated("id", datasetId, "polyps");
  }
  
  @Override
  public int countPolypRecordingsInDatasets(String datasetId) {
    final PolypDataset dataset = this.dh.get(datasetId)
      .orElseThrow(() -> new IllegalArgumentException("Dataset not found: " + datasetId));
    
    final CriteriaBuilder cb = this.dhPolypRecording.cb();
    CriteriaQuery<Long> query = cb.createQuery(Long.class);

    final Root<PolypRecording> root = query.from(this.dhPolypRecording.getEntityType());
    
    query = query.select(cb.count(root))
      .where(cb.isMember(dataset, root.join("polyp").get("polypDatasets")));

    return this.em.createQuery(query).getSingleResult().intValue();
  }
  
  @Override
  public PolypDataset createPolypDataset(PolypDataset polypDataset) {
    return this.dh.persist(polypDataset);
  }
  
  @Override
  public PolypDataset editPolypDataset(PolypDataset polypDataset) {
    return this.dh.update(polypDataset);
  }
  
  @Override
  public void deletePolypDataset(String datasetId) {
    this.dh.removeByKey(datasetId);
  }
}
