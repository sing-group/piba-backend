package org.sing_group.piba.service.video;

import java.io.File;

public class VideoConversionTask {
  public enum ConversionTaskStatus {
    ON_PROGRESS, FINISHED_SUCCESS, FINISHED_ERROR
  }

  private String id;
  private String sourceFormat;
  private String destinationFormat;
  private File input;
  private File output;
  private ConversionTaskStatus status = ConversionTaskStatus.ON_PROGRESS;

  public VideoConversionTask(String id, String sourceFormat, String destinationFormat, File input, File output) {
    super();
    this.id = id;
    this.sourceFormat = sourceFormat;
    this.destinationFormat = destinationFormat;
    this.input = input;
    this.output = output;
  }

  public String getId() {
    return id;
  }

  public String getSourceFormat() {
    return sourceFormat;
  }

  public String getDestinationFormat() {
    return destinationFormat;
  }

  public File getInput() {
    return input;
  }

  public File getOutput() {
    return output;
  }
  
  public ConversionTaskStatus getStatus() {
    return status;
  }
  
  public void setStatus(ConversionTaskStatus status) {
    this.status = status;
  }
  
}
