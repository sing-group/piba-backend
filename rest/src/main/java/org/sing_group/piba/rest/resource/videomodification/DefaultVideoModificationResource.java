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

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.domain.entities.videomodification.VideoModification;
import org.sing_group.piba.rest.entity.mapper.spi.VideoModificationMapper;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationData;
import org.sing_group.piba.rest.entity.videomodification.VideoModificationEditionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.videomodification.VideoModificationResource;
import org.sing_group.piba.service.spi.modifier.ModifierService;
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
        video, modifier, videoModificationEditionData.getStart(), videoModificationEditionData.getEnd()
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
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown video modification: {id}")
  )
  @Override
  public Response delete(@PathParam("id") int id) {
    this.service.delete(this.service.get(id));
    return Response.ok().build();
  }

}
