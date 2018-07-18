package org.sing_group.piba.service.video;

import static javax.ejb.TransactionAttributeType.NEVER;
import static javax.ejb.TransactionManagementType.BEAN;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.piba.service.spi.video.VideoConversionService;

@Stateless
@PermitAll
@TransactionManagement(BEAN)
@TransactionAttribute(NEVER)
public class DefaultVideoConversionService implements VideoConversionService {

  @Inject
  private Event<VideoConversionTask> conversionEvent;

  @Asynchronous
  @Override
  public void convertVideo(VideoConversionTask task) {
    //TODO: conversion.....
    task.setStatus(VideoConversionTask.ConversionTaskStatus.FINISHED_SUCCESS);
    conversionEvent.fire(task);

  }

}
