package org.sing_group.piba.service.storage;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.piba.service.spi.storage.FileStorage;
import org.sing_group.piba.service.spi.storage.StorageCleaner;
import org.sing_group.piba.service.spi.video.VideoService;

@Default
@Startup
@Singleton
public class DefaultStorageCleaner implements StorageCleaner {

  @Resource
  private SessionContext sessionContext;

  @Inject
  private FileStorage videoStorage;

  @Inject
  private VideoService videoService;

  private Timer timer;

  @PostConstruct
  @Override
  public void runCleaners() {
    this.timer = sessionContext.getTimerService().createTimer(0, TimeUnit.DAYS.toMillis(1), null);
  }

  @Timeout
  public void timeOutHandler() {
    for (String id : videoStorage.getAllIds()) {
      if (!videoService.existsVideo(id)) {
        videoStorage.delete(id);
      }
    }
  }

  @PreDestroy
  public void shutdown() {
    this.timer.cancel();

  }

}
