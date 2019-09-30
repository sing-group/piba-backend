/*-
 * #%L
 * REST
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
package org.sing_group.piba.rest.resource.videomodification;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.domain.entities.user.Role;
import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.domain.entities.videomodification.VideoModification;
import org.sing_group.piba.rest.entity.mapper.spi.VideoModificationMapper;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationData;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationEditionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.mapper.SecurityExceptionMapper;
import org.sing_group.piba.rest.resource.spi.videomodification.VideoModificationResource;
import org.sing_group.piba.service.spi.modifier.ModifierService;
import org.sing_group.piba.service.spi.user.UserService;
import org.sing_group.piba.service.spi.video.VideoService;
import org.sing_group.piba.service.spi.videomodification.VideoModificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
  private UserService userService;

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
  @ApiResponses(value = {
    @ApiResponse(code = 400, message = "Unknown video"),
    @ApiResponse(code = 400, message = "Unknown modifier")
  })
  @Override
  public Response create(VideoModificationEditionData videoModificationEditionData) {
    Video video = this.videoService.getVideo(videoModificationEditionData.getVideo());
    Modifier modifier = this.modifierService.get(videoModificationEditionData.getModifier());

    VideoModification videoModification =
      new VideoModification(
        video, modifier, videoModificationEditionData.getStart(), videoModificationEditionData.getEnd(),
        videoModificationEditionData.isConfirmed()
      );

    this.service.create(videoModification);

    return Response.created(UriBuilder.fromResource(DefaultVideoModificationResource.class).build())
      .entity(videoModificationMapper.toVideoModificationData(videoModification)).build();
  }

  @GET
  @ApiOperation(
    value = "Returns the modifiers in that video.", response = VideoModificationData.class, responseContainer = "List", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown video: {id}")
  )
  @Override
  public Response getVideoModification(@QueryParam("id") String video_id) {
    Video video = this.videoService.getVideo(video_id);
    return Response.ok(
      this.service.getVideoModification(video).map(this.videoModificationMapper::toVideoModificationData).toArray(VideoModificationData[]::new)
    ).build();
  }

  @DELETE
  @Path("{id}")
  @ApiOperation(
    value = "Deletes an existing video modification.", code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown video modification: {id}"),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response delete(@PathParam("id") int id) {
    VideoModification videoModification = this.service.get(id);
    if (!videoModification.isConfirmed()) {
      this.service.delete(videoModification);
      return Response.ok().build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  @Path("{id}")
  @PUT
  @ApiOperation(
    value = "Modifies an exisiting video modification", response = VideoModification.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown video modification: {id}"),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response edit(@PathParam("id") int id, VideoModificationEditionData videoModificationEditionData) {
    VideoModification videoModification = this.service.get(id);
    if (isVideoModificationEditable(videoModification, videoModificationEditionData)) {
      this.videoModificationMapper.assignVideoModificationEditionData(videoModification, videoModificationEditionData);
      return Response.ok(this.videoModificationMapper.toVideoModificationData(this.service.edit(videoModification)))
        .build();
    } else {
      return Response.status(Status.FORBIDDEN).build();
    }
  }

  @PUT
  @ApiOperation(
    value = "Modifies all existing video modifications in the array", response = VideoModificationData.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown video modification."),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response editAll(VideoModificationEditionData[] videoModificationsEditionData) {
    if (
      Arrays.stream(videoModificationsEditionData).allMatch(
        videoModificationEditionData -> isVideoModificationEditable(
          this.service.get(videoModificationEditionData.getId()), videoModificationEditionData
        )
      )
    ) {
      List<VideoModification> editedVideoModifications = new ArrayList<VideoModification>();
      Arrays.stream(videoModificationsEditionData).forEach(videoModificationEditionData -> {
        VideoModification videoModification = this.service.get(videoModificationEditionData.getId());
        this.videoModificationMapper
          .assignVideoModificationEditionData(videoModification, videoModificationEditionData);
        editedVideoModifications.add(this.service.edit(videoModification));
      });
      return Response.ok(
        editedVideoModifications.stream().map(this.videoModificationMapper::toVideoModificationData)
          .toArray(VideoModificationData[]::new)
      ).build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  private boolean isEndoscopist() {
    User currentUser = this.userService.getCurrentUser();
    return currentUser.getRole().equals(Role.ENDOSCOPIST);
  }

  private boolean isVideoModificationEditable(
    VideoModification videoModification, VideoModificationEditionData videoModificationEditionData
  ) {
    return (!videoModification.isConfirmed() && videoModificationEditionData.isConfirmed() && isEndoscopist())
      || (!videoModification.isConfirmed() && !videoModificationEditionData.isConfirmed());
  }

}
