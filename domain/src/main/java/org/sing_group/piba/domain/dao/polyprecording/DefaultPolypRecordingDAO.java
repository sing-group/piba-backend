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
package org.sing_group.piba.domain.dao.polyprecording;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.polyprecording.PolypRecordingDAO;
import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;
import org.sing_group.piba.domain.entities.video.Video;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultPolypRecordingDAO implements PolypRecordingDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, PolypRecording> dh;

  public DefaultPolypRecordingDAO() {
    super();
  }

  public DefaultPolypRecordingDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, PolypRecording.class, this.em);
  }

  @Override
  public Stream<PolypRecording> get(Video video) {
    return this.dh.listBy("video", video).stream();
  }

  @Override
  public Stream<PolypRecording> get(Polyp polyp) {
    return this.dh.listBy("polyp", polyp).stream();
  }

  @Override
  public PolypRecording create(PolypRecording polypRecording) {
    return this.dh.persist(polypRecording);
  }

  @Override
  public void delete(PolypRecording polypRecording) {
     this.dh.remove(polypRecording);
  }
  
  @Override
  public PolypRecording get(int id) {
    return this.dh.get(id).orElseThrow(() -> new IllegalArgumentException("Unknown polyp recording: " + id));
  }

}
