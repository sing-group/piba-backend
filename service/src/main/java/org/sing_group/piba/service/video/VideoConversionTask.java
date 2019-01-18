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
package org.sing_group.piba.service.video;

import static java.util.Arrays.asList;

import java.io.File;
import java.util.List;

public class VideoConversionTask {
  public enum ConversionTaskStatus {
    ON_PROGRESS, FINISHED_SUCCESS, FINISHED_ERROR
  }

  private String id;
  private Integer fps;
  private File input;
  private FileAndFormat[] outputs;
  private ConversionTaskStatus status = ConversionTaskStatus.ON_PROGRESS;

  public VideoConversionTask(String id, File input, FileAndFormat ... outputs) {
    this.id = id;
    this.input = input;
    this.outputs = outputs;
  }

  public String getId() {
    return id;
  }
  
  public Integer getFps() {
    return fps;
  }

  public void setFps(Integer fps) {
    this.fps = fps;
  }

  public File getInput() {
    return input;
  }
  
  public List<FileAndFormat> getOutputs() {
    return asList(outputs);
  }

  public ConversionTaskStatus getStatus() {
    return status;
  }

  public void setStatus(ConversionTaskStatus status) {
    this.status = status;
  }

  public static class FileAndFormat {
    private final String format;
    private final File file;

    public FileAndFormat(String format, File file) {
      this.format = format;
      this.file = file;
    }

    public String getFormat() {
      return format;
    }

    public File getFile() {
      return file;
    }

  }

}
