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

import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.rest.entity.mapper.spi.polyprecording.PolypRecordingMapper;
import org.sing_group.piba.rest.entity.polyptrecording.PolypRecordingData;
import org.sing_group.piba.rest.entity.polyptrecording.PolypRecordingEditicionData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.polyp.DefaultPolypResource;
import org.sing_group.piba.rest.resource.spi.polyprecording.PolypRecordingResource;
import org.sing_group.piba.service.spi.polyp.PolypService;
import org.sing_group.piba.service.spi.polyprecording.PolypRecordingService;
import org.sing_group.piba.service.spi.video.VideoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
  private PolypRecordingMapper polypRecordingMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.polypRecordingMapper.setRequestURI(this.uriInfo);
  }

  @GET
  @ApiOperation(value = "Returns the polyps recorded in that video.", response = PolypRecordingData.class, code = 200)
  @Override
  public Response getPolypResource(@QueryParam("id") String video_id) {
    Video video = this.videoService.getVideo(video_id);
    return Response.ok(
      this.polypRecordingService.get(video).map(this.polypRecordingMapper::toPolypRecordingData).toArray(PolypRecordingData[]::new)
    ).build();
  }

  @POST
  @ApiOperation(value = "Creates a new relationship between a polyp and a video.", response = PolypRecordingData.class, code = 200)
  @Override
  public Response create(PolypRecordingEditicionData polypRecordingEditicionData) {
    Polyp polyp = this.polypService.getPolyp(polypRecordingEditicionData.getPolyp());
    Video video = this.videoService.getVideo(polypRecordingEditicionData.getVideo());
   
    PolypRecording polypRecording =
      new PolypRecording(
        polyp, video, polypRecordingEditicionData.getStart(), polypRecordingEditicionData.getEnd()
      );
    
    this.polypRecordingService.create(polypRecording);

    return Response.created(UriBuilder.fromResource(DefaultPolypResource.class).build())
      .entity(polypRecordingMapper.toPolypRecordingData(polypRecording)).build();
  }

}
