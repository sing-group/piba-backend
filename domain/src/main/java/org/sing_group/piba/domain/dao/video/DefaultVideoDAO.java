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

package org.sing_group.piba.domain.dao.video;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.video.VideoDAO;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.domain.entities.videomodification.VideoModification;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultVideoDAO implements VideoDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, Video> dh;
  protected DAOHelper<Integer, VideoModification> dhVideoModification;

  public DefaultVideoDAO() {
    super();
  }

  public DefaultVideoDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, Video.class, this.em);
    this.dhVideoModification = DAOHelper.of(Integer.class, VideoModification.class, this.em);
  }

  @Override
  public Stream<Video> getVideos() {
    return dh.list().stream();
  }

  @Override
  public Video getVideo(String id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown video: " + id));
  }
  
  @Override
  public boolean existsVideo(String id) {
    return this.dh.get(id).isPresent();
  }

  @Override
  public Video create(Video video) {
    return this.dh.persist(video);
  }

  @Override
  public Video edit(Video video) {
    return this.dh.update(video);
  }

  @Override
  public void delete(Video video) {
    for (VideoModification videoModification : this.dhVideoModification.listBy("video", video)) {
      this.dhVideoModification.remove(videoModification);
    }
    this.dh.remove(video);
  }

}
