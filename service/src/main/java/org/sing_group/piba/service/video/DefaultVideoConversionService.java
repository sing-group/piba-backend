package org.sing_group.piba.service.video;

import static javax.ejb.TransactionAttributeType.NEVER;
import static javax.ejb.TransactionManagementType.BEAN;

import java.io.File;
import java.io.IOException;

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
    
    if (!input.isFile() || ! input.canRead()) {
      throw new IllegalArgumentException("File " + input.getAbsolutePath() + " not found.");
    }
    
    for (FileAndFormat output : task.getOutputs()) {
      this.convert(input, output.getFile(), output.getFormat());
    }
    
    task.setStatus(VideoConversionTask.ConversionTaskStatus.FINISHED_SUCCESS);
    
    conversionEvent.fire(task);
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
