package org.sing_group.piba.service.storage;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.piba.service.spi.storage.FileStorage;
import org.sing_group.piba.service.spi.storage.StorageCleaner;
import org.sing_group.piba.service.spi.video.VideoService;

@Default
@Startup
@Singleton
public class DefaultStorageCleaner implements StorageCleaner {

  @Resource(name = "MyScheduledExectutorService")
  private ManagedScheduledExecutorService executorService;

  @Inject
  private FileStorage videoStorage;

  @Inject
  private VideoService videoService;

  @PostConstruct
  @Override
  public void runCleaners() {
    executorService.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        for (String id : videoStorage.getAllIds()) {
          if (!videoService.existsVideo(id)) {
            videoStorage.delete(id);
          }
        }
      }
    }, 0L, 1L, TimeUnit.DAYS
    );
  }

}
