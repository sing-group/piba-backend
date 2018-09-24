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
    switch (task.getSourceFormat()) {
      case "mp4":
        if (task.getDestinationFormat().equals("ogg")) {
          convertMp4ToOgg(task.getInput(), task.getOutput());
        }
        break;
      case "ogg":
        if (task.getDestinationFormat().equals("mp4")) {
          convertOggToMp4(task.getInput(), task.getOutput());
        }
        break;
      default:
        break;
    }
    task.setStatus(VideoConversionTask.ConversionTaskStatus.FINISHED_SUCCESS);
    conversionEvent.fire(task);

  }

  public void convertOggToMp4(File ogg, File mp4) {
    if (exists(ogg)) {
      convert(ogg, mp4, "mp4");
    }
  }

  public void convertMp4ToOgg(File mp4, File ogg) {
    if (exists(mp4)) {
      convert(mp4, ogg, "ogg");
    }
  }

  private boolean exists(File file) {
    if (!file.exists()) {
      throw new IllegalArgumentException("File " + file.getAbsolutePath() + " not found.");
    }
    return true;
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
