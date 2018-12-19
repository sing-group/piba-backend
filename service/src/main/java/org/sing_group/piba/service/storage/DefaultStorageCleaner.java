/*-
 * #%L
 * Service
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

import org.sing_group.piba.service.spi.image.ImageService;
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
  private FileStorage fileStorage;

  @Inject
  private VideoService videoService;

  @Inject
  private ImageService imageService;

  private Timer timer;

  @PostConstruct
  @Override
  public void runCleaners() {
    this.timer = sessionContext.getTimerService().createTimer(0, TimeUnit.DAYS.toMillis(1), null);
  }

  @Timeout
  public void timeOutHandler() {
    for (String id : fileStorage.getAllIds()) {
      if (!videoService.existsVideo(id) && !imageService.existsImage(id)) {
        fileStorage.delete(id);
      }
    }
  }

  @PreDestroy
  public void shutdown() {
    this.timer.cancel();
  }

}
