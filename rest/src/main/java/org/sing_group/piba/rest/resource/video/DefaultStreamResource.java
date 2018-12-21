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
package org.sing_group.piba.rest.resource.video;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.sing_group.piba.rest.filter.CrossDomain;
import org.sing_group.piba.rest.resource.spi.video.StreamResource;
import org.sing_group.piba.service.spi.storage.FileStorage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("stream")
@Api(value = "stream")
@Produces({
  APPLICATION_JSON, APPLICATION_XML
})
@Consumes({
  APPLICATION_JSON, APPLICATION_XML
})
@Stateless
@Default
@CrossDomain
public class DefaultStreamResource implements StreamResource {

  @Inject
  private FileStorage fileStorage;

  @Path("{id}")
  @GET
  @ApiOperation(
    value = "Return the stream of a video.", response = StreamingOutput.class, code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown video: {id}")
  )
  public Response getVideoStream(
    @PathParam(
      "id"
    ) String id,
    @QueryParam(
      "format"
    ) String format,
    @HeaderParam("range") String rangeHeader
  ) {
    final InputStream videoStream = this.fileStorage.retrieve(id, format);
    final long fileLength = this.fileStorage.getLength(id, format);
    Range range = getRange(rangeHeader, fileLength);
    ResponseBuilder responseBuilder =
      Response
        .ok(new StreamingOutput() {
          @Override
          public void write(OutputStream output) throws IOException, WebApplicationException {
            byte[] buf = new byte[8192];
            int len = -1;
            videoStream.skip(range.start);
            long toWrite = range.getLength();
            while (toWrite > 0 && (len = videoStream.read(buf)) != -1) {
              try {
                output.write(buf, 0, (int) Math.min(len, toWrite));
                toWrite -= Math.min(len, toWrite);
              } catch (IOException e) {
                if (e.getMessage().toUpperCase().contains("RESET") || e.getMessage().toUpperCase().contains("BROKEN")) {
                  // catched "Connection reset by peer" or "Broken pipe", which
                  // is
                  // normal when client closes unilaterally
                } else {
                  throw e;
                }
              }
            }
          }
        }
        )
        .type("video/" + format)
        .header("Accept-Ranges", "bytes")
        .header("Content-Length", range.stop - range.start + 1);

    if (rangeHeader != null) {
      responseBuilder.status(Status.fromStatusCode(206));
      responseBuilder.header("Content-Range", "bytes " + range.start + "-" + range.stop + "/" + fileLength);
    }

    return responseBuilder.build();
  }

  private Range getRange(String rangeHeader, long length) {
    if (rangeHeader == null) {
      return new Range(0, length - 1);
    }
    String[] rangeTokens = rangeHeader.substring(rangeHeader.indexOf("=") + 1).split("-", 2);
    return new Range(
      rangeTokens[0].equals("") ? 0 : Long.parseLong(rangeTokens[0]), rangeTokens[1].equals("") ? length - 1 : Long.parseLong(rangeTokens[1]));
  }

  private static class Range {
    public long start, stop;

    public Range(long start, long stop) {
      super();
      this.start = start;
      this.stop = stop;
    }

    public long getLength() {
      return stop - start + 1;
    }
  }
}
