package org.sing_group.piba.rest.resource.videomodification;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.domain.entities.videomodification.VideoModification;
import org.sing_group.piba.rest.entity.mapper.spi.videomodification.VideoModificationMapper;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationData;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationEditionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.videomodification.VideoModificationResource;
import org.sing_group.piba.service.spi.modifier.ModifierService;
import org.sing_group.piba.service.spi.video.VideoService;
import org.sing_group.piba.service.spi.videomodification.VideoModificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("videomodification")
@Api(value = "videomodification")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultVideoModificationResource implements VideoModificationResource {

  @Inject
  private VideoModificationService service;

  @Inject
  private VideoService videoService;

  @Inject
  private ModifierService modifierService;

  @Inject
  private VideoModificationMapper videoModificationMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.videoModificationMapper.setRequestURI(this.uriInfo);
  }

  @POST
  @ApiOperation(
    value = "Creates a new relationship between a video and a modifier.", response = VideoModificationData.class, code = 200
  )
  @Override
  public Response create(VideoModificationEditionData videoModificationEditionData) {
    Video video = this.videoService.getVideo(videoModificationEditionData.getVideo());
    Modifier modifier = this.modifierService.get(videoModificationEditionData.getModifier());

    VideoModification videoModification =
      new VideoModification(
        video, modifier, videoModificationEditionData.getStart(), videoModificationEditionData.getEnd()
      );

    this.service.create(videoModification);

    return Response.created(UriBuilder.fromResource(DefaultVideoModificationResource.class).build())
      .entity(videoModificationMapper.toVideoModificationData(videoModification)).build();
  }

  @GET
  @ApiOperation(value = "Returns the modifiers in that video.", response = VideoModificationData.class, code = 200)
  @Override
  public Response getVideoModification(@QueryParam("id") String video_id) {
    Video video = this.videoService.getVideo(video_id);
    return Response.ok(
      this.service.getVideoModification(video).map(this.videoModificationMapper::toVideoModificationData).toArray(VideoModificationData[]::new)
    ).build();
  }

}
