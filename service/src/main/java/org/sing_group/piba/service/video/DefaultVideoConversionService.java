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

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static javax.ejb.TransactionAttributeType.NEVER;
import static javax.ejb.TransactionManagementType.BEAN;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.piba.service.spi.video.VideoConversionService;
import org.sing_group.piba.service.video.VideoConversionTask.FileAndFormat;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

@Stateless
@PermitAll
@TransactionManagement(BEAN)
@TransactionAttribute(NEVER)
public class DefaultVideoConversionService implements VideoConversionService {

  @Inject
  private Event<VideoConversionTask> conversionEvent;

  private String pathToFFmpeg = "/usr/bin/ffmpeg";
  private String pathToFFprobe = "/usr/bin/ffprobe";

  @Asynchronous
  @Override
  public void convertVideo(VideoConversionTask task) {
    final File input = task.getInput();

    if (!input.isFile() || !input.canRead()) {
      throw new IllegalArgumentException("File " + input.getAbsolutePath() + " not found.");
    }

    try {
      int fps = getVideoFrameRate(input);
      task.setFps(fps);

      for (FileAndFormat output : task.getOutputs()) {
        this.convert(input, output.getFile(), output.getFormat());
      }
      task.setStatus(VideoConversionTask.ConversionTaskStatus.FINISHED_SUCCESS);

    } catch (IOException e) {
      task.setStatus(VideoConversionTask.ConversionTaskStatus.FINISHED_ERROR);
    }
    conversionEvent.fire(task);
  }

  private int getVideoFrameRate(final File input) throws IOException {
    String query = "ffmpeg -i " + input.toString() + " 2>&1 | sed -n 's/.*, \\(.*\\) fp.*/\\1/p'";
    List<String> command = new ArrayList<>(asList("/bin/bash", "-c"));
    command.add(query);
    Process process = new ProcessBuilder(command).start();
    return parseInt(new BufferedReader(new InputStreamReader(process.getInputStream())).readLine());
  }

  private void convert(File from, File to, String toFormat) {
    try {
      FFmpeg ffmpeg = new FFmpeg(pathToFFmpeg);
      FFprobe ffprobe = new FFprobe(pathToFFprobe);
      runConvert(ffmpeg, ffprobe, builder(from, to, toFormat));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private FFmpegBuilder builder(File from, File to, String toFormat) {
    return new FFmpegBuilder()
      .setInput(from.getAbsolutePath())
      .overrideOutputFiles(true)
      .addOutput(to.toString())
      .setFormat(toFormat)
      .addExtraArgs("-qscale:v", "6")
      .done();
  }

  private void runConvert(FFmpeg ffmpeg, FFprobe ffprobe, FFmpegBuilder builder) {
    FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
    executor.createJob(builder).run();
  }

}
