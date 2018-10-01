package org.sing_group.piba.service.video;

import static java.util.Arrays.asList;

import java.io.File;
import java.util.List;

public class VideoConversionTask {
  public enum ConversionTaskStatus {
    ON_PROGRESS, FINISHED_SUCCESS, FINISHED_ERROR
  }

  private String id;
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
