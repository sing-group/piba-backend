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
package org.sing_group.piba.rest.resource.polyp;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.rest.entity.mapper.spi.PolypDatasetMapper;
import org.sing_group.piba.rest.entity.mapper.spi.PolypMapper;
import org.sing_group.piba.rest.entity.mapper.spi.PolypRecordingMapper;
import org.sing_group.piba.rest.entity.polyp.PolypData;
import org.sing_group.piba.rest.entity.polyp.PolypDatasetData;
import org.sing_group.piba.rest.entity.polyprecording.PolypRecordingData;
import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.polyp.PolypDatasetResource;
import org.sing_group.piba.service.spi.polyp.PolypDatasetService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("polypdataset")
@Api(value = "polypdataset")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain(allowedHeaders = "X-Pagination-Total-Items")
public class DefaultPolypDatasetResource implements PolypDatasetResource {

  @Inject
  private PolypDatasetService service;

  @Inject
  private PolypDatasetMapper polypDatasetMapper;

  @Inject
  private PolypMapper polypMapper;
  
  @Inject
  private PolypRecordingMapper polypRecordingMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void init() {
    this.polypDatasetMapper.setRequestURI(this.uriInfo);
    this.polypMapper.setRequestURI(this.uriInfo);
    this.polypRecordingMapper.setRequestURI(this.uriInfo);
  }

  @Path("{id}")
  @GET
  @ApiOperation(
    value = "Return the data of a polyp dataset.", response = PolypDatasetData.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown polyp dataset: {id}")
  )
  @Override
  public Response getPolypDataset(
    @PathParam("id") String id
  ) {
    return Response
      .ok(this.polypDatasetMapper.toPolypDatasetData(this.service.getPolypDataset(id)))
      .build();
  }

  @GET
  @ApiOperation(
    value = "Returns the data of all the polyp datasets.", response = PolypData.class, responseContainer = "List", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Invalid page or pageSize. They must be an integer.")
  )
  @Override
  public Response listPolypDatasets(
    @QueryParam("page") int page, @QueryParam("pageSize") int pageSize
  ) {
    int countPolyps = this.service.countPolypDatasets();

    return Response.ok(
      this.service.listPolypDatasets(page, pageSize)
        .map(this.polypDatasetMapper::toPolypDatasetData)
        .toArray(PolypDatasetData[]::new)
    )
      .header("X-Pagination-Total-Items", countPolyps)
      .build();
  }

  @GET
  @Path("{id}/polyp")
  @ApiOperation(
    value = "Returns the polyps of a polyp datasets.",
    response = PolypData.class, responseContainer = "List", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Invalid page or pageSize. They must be an integer.")
  )
  @Override
  public Response listPolypsOfPolypDatasets(
    @PathParam("id") String datasetId,
    @QueryParam("page") int page,
    @QueryParam("pageSize") int pageSize
  ) {
    int countPolyps = this.service.countPolypsInDatasets(datasetId);

    return Response.ok(
      this.service.listPolypsInDatasets(datasetId, page, pageSize)
        .map(this.polypMapper::toPolypData)
        .toArray(PolypData[]::new)
    )
      .header("X-Pagination-Total-Items", countPolyps)
      .build();
  }

  @GET
  @Path("{id}/polyprecording")
  @ApiOperation(
    value = "Returns the polpy recorgings of a polyp datasets.",
    response = PolypRecordingData.class, responseContainer = "List", code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Invalid page or pageSize. They must be an integer.")
  )
  @Override
  public Response listPolypRecordingsOfPolypDatasets(
    @PathParam("id") String datasetId,
    @QueryParam("page") int page,
    @QueryParam("pageSize") int pageSize
  ) {
    int countPolyps = this.service.countPolypRecordingsInDatasets(datasetId);

    return Response.ok(
      this.service.listPolypRecordingsInDatasets(datasetId, page, pageSize)
        .map(this.polypRecordingMapper::toPolypRecordingData)
      .toArray(PolypRecordingData[]::new)
    )
      .header("X-Pagination-Total-Items", countPolyps)
    .build();
  }

}
