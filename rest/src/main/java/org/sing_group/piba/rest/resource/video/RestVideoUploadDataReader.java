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
package org.sing_group.piba.rest.resource.video;

import java.io.File;

import javax.ws.rs.ext.Provider;

import org.sing_group.piba.rest.MultipartMessageBodyReader;
import org.sing_group.piba.rest.entity.RestVideoUploadData;

@Provider
public class RestVideoUploadDataReader extends MultipartMessageBodyReader<RestVideoUploadData> {

  private String title, observations, withText, exploration_id;
  private File videoData;

  @Override
  protected void add(String name, String value) {
    switch (name) {
      case "title":
        this.title = value;
        break;
      case "observations":
        this.observations = value;
        break;
      case "withText":
        this.withText = value;
        break;
      case "exploration_id":
        this.exploration_id = value;
        break;
    }

  }

  @Override
  protected void add(String name, File uploadedFile) {
    switch (name) {
      case "video":
        this.videoData = uploadedFile;
        break;
    }

  }

  @Override
  protected RestVideoUploadData build() {
    return new RestVideoUploadData(this.title, this.observations, this.videoData, this.withText, this.exploration_id);
  }

  @Override
  protected void init() {}

}
