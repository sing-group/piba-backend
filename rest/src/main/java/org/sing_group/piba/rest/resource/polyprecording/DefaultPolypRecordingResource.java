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
package org.sing_group.piba.rest.resource.polyprecording;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;
import org.sing_group.piba.domain.entities.user.Role;
import org.sing_group.piba.domain.entities.user.User;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.rest.entity.mapper.spi.PolypRecordingMapper;
import org.sing_group.piba.rest.entity.polyp.PolypData;
import org.sing_group.piba.rest.entity.polyprecording.PolypRecordingData;
import org.sing_group.piba.rest.entity.polyprecording.PolypRecordingEditionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.mapper.SecurityExceptionMapper;
import org.sing_group.piba.rest.resource.spi.polyprecording.PolypRecordingResource;
import org.sing_group.piba.service.spi.polyp.PolypService;
import org.sing_group.piba.service.spi.polyprecording.PolypRecordingService;
import org.sing_group.piba.service.spi.user.UserService;
import org.sing_group.piba.service.spi.video.VideoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("polyprecording")
@Api(value = "polyprecording")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultPolypRecordingResource implements PolypRecordingResource {

  @Inject
  private PolypRecordingService polypRecordingService;

  @Inject
  private VideoService videoService;

  @Inject
  private PolypService polypService;

  @Inject
  private UserService userService;

  @Inject
  private PolypRecordingMapper polypRecordingMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.polypRecordingMapper.setRequestURI(this.uriInfo);
  }


  @GET
  @Path("/{id}")
  @ApiOperation(
    value = "Returns a polyp recording.", response = PolypRecordingData.class, code = 200
  )
  @ApiResponses(@ApiResponse(code = 400, message = "Unknown polyp recording: {id}"))
  @Override
  public Response getPolypRecording(@PathParam("id") int id) {
    return Response.ok(
      this.polypRecordingMapper.toPolypRecordingData(this.polypRecordingService.get(id))
    ).build();
  }

  @GET
  @Path("/video/{video_id}")
  @ApiOperation(
    value = "Returns the polyps recorded in that video.", response = PolypRecordingData.class, responseContainer = "List", code = 200
  )
  @ApiResponses(@ApiResponse(code = 400, message = "Unknown video: {video_id}"))
  @Override
  public Response listPolypResourcesByVideo(@PathParam("video_id") String videoId) {
    Video video = this.videoService.getVideo(videoId);
    return Response.ok(
      this.polypRecordingService.listByVideo(video).map(this.polypRecordingMapper::toPolypRecordingData).toArray(PolypRecordingData[]::new)
    ).build();
  }

  @GET
  @Path("/polyp/{polyp_id}")
  @ApiOperation(
    value = "Returns the polyps recorded in that polyp.", response = PolypRecordingData.class, responseContainer = "List", code = 200
  )
  @ApiResponses(@ApiResponse(code = 400, message = "Unknown polyp: {polyp_id}"))
  @Override
  public Response listPolypResourcesByPolyp(@PathParam("polyp_id") String polypId) {
    Polyp polyp = this.polypService.getPolyp(polypId);
    return Response.ok(
      this.polypRecordingService.listByPolyp(polyp).map(this.polypRecordingMapper::toPolypRecordingData).toArray(PolypRecordingData[]::new)
    ).build();
  }

  @POST
  @ApiOperation(
    value = "Creates a new relationship between a polyp and a video.", response = PolypRecordingData.class, code = 200
  )
  @ApiResponses(value = {
    @ApiResponse(code = 400, message = "Unknown polyp."),
    @ApiResponse(code = 400, message = "Unknown video.")
  })
  @Override
  public Response create(PolypRecordingEditionData polypRecordingEditionData) {
    Polyp polyp = this.polypService.getPolyp(polypRecordingEditionData.getPolyp());
    Video video = this.videoService.getVideo(polypRecordingEditionData.getVideo());

    PolypRecording polypRecording =
      new PolypRecording(
        polyp, video, polypRecordingEditionData.getStart(), polypRecordingEditionData.getEnd(), false
      );

    this.polypRecordingService.create(polypRecording);

    return Response.created(UriBuilder.fromResource(DefaultPolypRecordingResource.class).build())
      .entity(polypRecordingMapper.toPolypRecordingData(polypRecording)).build();
  }

  @DELETE
  @Path("{id}")
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown polyp recording: {id}"),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @ApiOperation(
    value = "Deletes an existing polyp recording.", code = 200
  )
  @Override
  public Response delete(@PathParam("id") int id) {
    PolypRecording polypRecording = this.polypRecordingService.get(id);
    if (!polypRecording.isConfirmed()) {
      this.polypRecordingService.delete(polypRecording);
      return Response.ok().build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  @Path("{id}")
  @PUT
  @ApiOperation(
    value = "Modifies an existing polyp recording", response = PolypRecordingData.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown polyp recording: {id}"),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response edit(@PathParam("id") int id, PolypRecordingEditionData polypRecordingEditionData) {
    System.out.println(polypRecordingEditionData);
    PolypRecording polypRecording = this.polypRecordingService.get(id);
    System.out.println(polypRecording);
    if (isPolypRecordingEditable(polypRecording, polypRecordingEditionData)) {
      this.polypRecordingMapper.assignPolypRecordingEditionData(polypRecording, polypRecordingEditionData);
      System.out.println(polypRecording);
      return Response
        .ok(this.polypRecordingMapper.toPolypRecordingData(this.polypRecordingService.edit(polypRecording)))
        .build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  @PUT
  @ApiOperation(
    value = "Modifies all existing polyp recordings in the array", response = PolypData.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown polyp recording."),
    @ApiResponse(code = 430, message = SecurityExceptionMapper.FORBIDDEN_MESSAGE)
  })
  @Override
  public Response editAll(PolypRecordingEditionData[] polypRecordingsEditionData) {
    if (
      Arrays.stream(polypRecordingsEditionData).allMatch(
        polypRecordingEditionData -> isPolypRecordingEditable(
          this.polypRecordingService.get(polypRecordingEditionData.getId()), polypRecordingEditionData
        )
      )
    ) {
      List<PolypRecording> editedPolypRecordings = new ArrayList<PolypRecording>();
      Arrays.stream(polypRecordingsEditionData).forEach(polypRecordingEditionData -> {
        PolypRecording polypRecording = this.polypRecordingService.get(polypRecordingEditionData.getId());
        this.polypRecordingMapper.assignPolypRecordingEditionData(polypRecording, polypRecordingEditionData);
        editedPolypRecordings.add(this.polypRecordingService.edit(polypRecording));
      });
      return Response.ok(
        editedPolypRecordings.stream().map(this.polypRecordingMapper::toPolypRecordingData).toArray(PolypRecordingData[]::new)
      ).build();
    } else {
      return Response.status(Response.Status.FORBIDDEN).build();
    }
  }

  private boolean isEndoscopist() {
    User currentUser = this.userService.getCurrentUser();
    return currentUser.getRole().equals(Role.ENDOSCOPIST);
  }

  private boolean isPolypRecordingEditable(
    PolypRecording polypRecording, PolypRecordingEditionData polypRecordingEditionData
  ) {
    return (!polypRecording.isConfirmed() && polypRecordingEditionData.isConfirmed() && isEndoscopist())
      || (!polypRecording.isConfirmed() && !polypRecordingEditionData.isConfirmed());
  }

}
