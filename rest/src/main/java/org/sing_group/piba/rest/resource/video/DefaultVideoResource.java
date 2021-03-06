/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Florentino Fdez-Riverola, Alba Nogueira Rodríguez
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

package org.sing_group.piba.rest.resource.video;

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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.rest.entity.RestVideoUploadData;
import org.sing_group.piba.rest.entity.mapper.spi.VideoMapper;
import org.sing_group.piba.rest.entity.video.VideoData;
import org.sing_group.piba.rest.entity.video.VideoEditionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.video.VideoResource;
import org.sing_group.piba.service.spi.video.VideoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("video")
@Api(value = "video")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultVideoResource implements VideoResource {
  @Inject
  private VideoService service;

  @Inject
  private VideoMapper videoMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.videoMapper.setRequestURI(this.uriInfo);
  }

  @Path("{id}")
  @GET
  @ApiOperation(
    value = "Return the data of a video.", response = VideoData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown video: {id}")
  )
  @Override
  public Response getVideo(
    @PathParam("id") String id
  ) {
    return Response
      .ok(this.videoMapper.toVideoData(this.service.getVideo(id)))
      .build();
  }

  @GET
  @ApiOperation(
    value = "Return the data of all videos.", response = VideoData.class, responseContainer = "List", code = 200
  )
  @Override
  public Response listVideos() {
    return Response.ok(this.service.getVideos().map(this.videoMapper::toVideoData).toArray(VideoData[]::new)).build();
  }

  @POST
  @ApiOperation(
    value = "Creates a new video.", responseHeaders = @ResponseHeader(
      name = "Location", description = "Location of the new video created.", response = VideoData.class
    ), code = 201
  )
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response create(RestVideoUploadData videoData) {
    Video video = this.service.create(videoData);

    return Response.created(UriBuilder.fromResource(DefaultVideoResource.class).path(video.getId()).build())
      .entity(videoMapper.toVideoData(video)).build();
  }

  @Path("{id}")
  @PUT
  @ApiOperation(
    value = "Modifies an existing video", response = VideoEditionData.class, code = 200
  )
  @Override
  public Response edit(@PathParam("id") String id, VideoEditionData videoEditionData) {
    Video video = this.service.getVideo(id);
    this.videoMapper.assignVideoEditionData(video, videoEditionData);
    return Response.ok(
      this.videoMapper.toVideoData(this.service.edit(video))
    )
      .build();
  }

  @DELETE
  @Path("{id}")
  @ApiOperation(
    value = "Deletes an existing video.", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown video: {id}")
  )
  @Override
  public Response delete(@PathParam("id") String id) {
    Video video = this.service.getVideo(id);
    this.service.delete(video);
    return Response.ok().build();
  }
}
