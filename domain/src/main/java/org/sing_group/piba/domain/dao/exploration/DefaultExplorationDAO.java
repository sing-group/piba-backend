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
package org.sing_group.piba.domain.dao.exploration;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.ListingOptions;
import org.sing_group.piba.domain.dao.ListingOptions.SortField;
import org.sing_group.piba.domain.dao.spi.exploration.ExplorationDAO;
import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.patient.Patient;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.video.Video;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultExplorationDAO implements ExplorationDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, Exploration> dh;
  protected DAOHelper<String, Polyp> dhPolyp;
  protected DAOHelper<String, Video> dhVideo;

  public DefaultExplorationDAO() {
    super();
  }

  public DefaultExplorationDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, Exploration.class, this.em);
    this.dhPolyp = DAOHelper.of(String.class, Polyp.class, this.em);
    this.dhVideo = DAOHelper.of(String.class, Video.class, this.em);
  }

  @Override
  public Exploration getExploration(String id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown exploration: " + id));
  }

  @Override
  public Stream<Exploration> listExplorations(int page, int pageSize, Patient patient) {
    int startBlock = (page - 1) * pageSize;
    int endBlock = startBlock + pageSize - 1;
    ListingOptions listingOptions = new ListingOptions(startBlock, endBlock, SortField.descending("creationDate"));
    // Checks if a patient is sent
    if (patient != null) {
      return dh.listBy("patient", patient, listingOptions).stream();
    } else {
      return dh.list(listingOptions).stream();
    }
  }

  @Override
  public int countExplorations() {
    return dh.list().size();
  }

  @Override
  public int countExplorationsByPatient(Patient patient) {
    return dh.listBy("patient", patient).size();
  }

  @Override
  public Exploration create(Exploration exploration) {
    return this.dh.persist(exploration);
  }

  @Override
  public Exploration edit(Exploration exploration) {
    return this.dh.update(exploration);
  }

  @Override
  public Stream<Polyp> listPolypsOfExploration(Exploration exploration) {
    return this.dhPolyp.listBy("exploration", exploration).stream();
  }

  @Override
  public Stream<Video> getVideos(Exploration exploration) {
    return this.dhVideo.listBy("exploration", exploration).stream();
  }

  @Override
  public void delete(Exploration exploration) {
    this.dh.remove(exploration);
  }

}
