package org.sing_group.piba.rest.resource.image;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.io.FileNotFoundException;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.image.DownloadResource;
import org.sing_group.piba.service.spi.image.GalleryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("download")
@Api(value = "download")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultDownloadResource implements DownloadResource {

  @Inject
  private GalleryService service;

  @GET
  @Path("/gallery/{id}/{filter}/{location}")
  @Produces("application/zip")
  @ApiOperation(
    value = "Return the images of a gallery in a zip.", response = StreamingOutput.class, code = 200
  )
  @ApiResponses({
    @ApiResponse(code = 400, message = "Unknown gallery: {id}"),
    @ApiResponse(code = 400, message = "Unknown filter: {filter}"),
    @ApiResponse(code = 400, message = "Unknown location: {location}")
  })
  @Override
  public Response getGalleryInZip(
    @PathParam("id") String id, @PathParam("filter") String filter, @PathParam("location") Boolean withLocation
  ) throws FileNotFoundException {
    String withLocationName = withLocation ? "_with_location" : "_without_location";
    return Response.ok(this.service.getGalleriesInZip(this.service.get(id), filter, withLocation))
      .header("Content-Disposition", "attachment; filename=\"" + id + "_" + filter + withLocationName + ".zip\"")
      .build();
  }

}
