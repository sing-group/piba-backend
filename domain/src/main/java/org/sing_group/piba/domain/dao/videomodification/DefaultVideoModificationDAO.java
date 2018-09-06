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
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.domain.entities.videomodification.VideoModification;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultVideoModificationDAO implements VideoModificationDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, VideoModification> dh;

  public DefaultVideoModificationDAO() {
    super();
  }

  public DefaultVideoModificationDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, VideoModification.class, this.em);
  }

  @Override
  public VideoModification create(VideoModification videoModification) {
    return this.dh.persist(videoModification);
  }

  @Override
  public Stream<VideoModification> getVideoModification(Video video) {
    return this.dh.listBy("video", video).stream();
  }

}
