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
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.ListingOptions;
import org.sing_group.piba.domain.dao.spi.polyp.PolypDatasetDAO;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyp.PolypDataset;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultPolypDatasetDAO implements PolypDatasetDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, PolypDataset> dh;
  protected DAOHelper<String, Polyp> dhPolyp;

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
  }
  
  @Override
  public PolypDataset getPolypDataset(String id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown polyp dataset: " + id));
  }

  @Override
  public Stream<PolypDataset> listPolypDatasets(int page, int pageSize) {
    int start = (page - 1) * pageSize;
    int end = start + pageSize - 1;
    
    ListingOptions listingOptions = ListingOptions.between(start, end).unsorted();
    
    return this.dh.list(listingOptions).stream();
  }
  
  @Override
  public Stream<Polyp> listPolypsInDataset(String datasetId, int page, int pageSize) {
    int start = (page - 1) * pageSize;
    int end = start + pageSize - 1;
    
    ListingOptions listingOptions = ListingOptions.between(start, end).unsorted();
    
    return this.dhPolyp.list(listingOptions, (cb, root) -> new Predicate[] {
      cb.equal(root.join("polypDatasets").get("id"), datasetId)
    }).stream();
  }
  
  @Override
  public int countPolypsInDataset(String datasetId) {
    return this.dh.countRelated("id", datasetId, "polyps");
  }
  
  @Override
  public int countPolypDatasets() {
    return this.dh.list().size();
  }

//  @Override
//  public Polyp getPolyp(String id) {
//    return this.dh.get(id).orElseThrow(() -> new IllegalArgumentException("Unknown polyp: " + id));
//  }
//
//  @Override
//  public Polyp create(Polyp polyp) {
//    return this.dh.persist(polyp);
//  }
//
//  @Override
//  public Polyp edit(Polyp polyp) {
//    return this.dh.update(polyp);
//  }
//
//  @Override
//  public void delete(Polyp polyp) {
//    this.dh.remove(polyp);
//  }

}
