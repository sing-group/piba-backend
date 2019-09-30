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
package org.sing_group.piba.domain.dao.videomodification;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.videomodification.VideoModificationDAO;
import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.domain.entities.videomodification.VideoModification;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultVideoModificationDAO implements VideoModificationDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<Integer, VideoModification> dh;

  public DefaultVideoModificationDAO() {
    super();
  }

  public DefaultVideoModificationDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, VideoModification.class, this.em);
  }

  @Override
  public VideoModification create(VideoModification videoModification) {
    return this.dh.persist(videoModification);
  }

  @Override
  public Stream<VideoModification> getVideoModification(Video video) {
    return this.dh.listBy("video", video).stream();
  }

  @Override
  public Stream<VideoModification> getVideoModification(Modifier modifier) {
    return this.dh.listBy("modifier", modifier).stream();
  }

  @Override
  public void delete(VideoModification videoModification) {
    this.dh.remove(videoModification);
  }

  @Override
  public VideoModification get(int id) {
    return this.dh.get(id).orElseThrow(() -> new IllegalArgumentException("Unknown video modification: " + id));
  }

  @Override
  public VideoModification edit(VideoModification videoModification) {
    return this.dh.update(videoModification);
  }

}
