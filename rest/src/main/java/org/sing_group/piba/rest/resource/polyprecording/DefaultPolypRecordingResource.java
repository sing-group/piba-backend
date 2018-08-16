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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.rest.entity.mapper.spi.polyprecording.PolypRecordingMapper;
import org.sing_group.piba.rest.entity.polyptrecording.PolypRecordingData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.polyprecording.PolypRecordingResource;
import org.sing_group.piba.service.spi.polyprecording.PolypRecordingService;
import org.sing_group.piba.service.spi.video.VideoService;

import io.swagger.annotations.Api;

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
  private PolypRecordingMapper polypRecordingMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.polypRecordingMapper.setRequestURI(this.uriInfo);
  }

  @GET
  @Override
  public Response getPolypResource(@QueryParam("id") String video_id) {
    return Response.ok(
      this.polypRecordingService.get(this.videoService.getVideo(video_id)).map(this.polypRecordingMapper::toPolypRecordingData).toArray(PolypRecordingData[]::new)
    ).build();
  }

}
