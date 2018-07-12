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

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.ext.Provider;

import org.sing_group.piba.rest.MultipartMessageBodyReader;
import org.sing_group.piba.rest.entity.RestVideoUploadData;

@Provider
public class RestVideoUploadDataReader extends MultipartMessageBodyReader<RestVideoUploadData> {

  private String title, observations;
  private InputStream videoData;

  @Override
  protected void add(String name, InputStream is) {
    try {
      switch (name) {
        case "title":
          this.title = new String(super.toByteArray(is));
          break;
        case "observations":
          this.observations = new String(super.toByteArray(is));
          break;
        case "video":
          this.videoData = is;
          break;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  protected RestVideoUploadData build() {
    return new RestVideoUploadData(this.title, this.observations, this.videoData);
  }

  @Override
  protected void init() { }

}
