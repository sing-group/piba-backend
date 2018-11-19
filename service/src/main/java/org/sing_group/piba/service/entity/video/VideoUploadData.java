/*-
 * #%L
 * REST
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

package org.sing_group.piba.service.entity.video;

import java.io.File;

public class VideoUploadData {

  private String title;

  private String observations;

  private File videoData;

  private String withText;

  private String exploration_id;

  public VideoUploadData(String title, String observations, File videoData, String withText, String exploration_id) {
    this.title = title;
    this.observations = observations;
    this.videoData = videoData;
    this.withText = withText;
    this.exploration_id = exploration_id;
  }

  public String getObservations() {
    return observations;
  }

  public String getTitle() {
    return title;
  }

  public File getVideoData() {
    return videoData;
  }

  public String getWithText() {
    return withText;
  }

  public String getExploration_id() {
    return exploration_id;
  }

}
