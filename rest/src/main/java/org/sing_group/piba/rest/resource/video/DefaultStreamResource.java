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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
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
    @QueryParam("format") String format
  ) {
    final InputStream videoStream = this.fileStorage.retrieve(id + "." + format);
    return Response
      .ok(new StreamingOutput() {
        @Override
        public void write(OutputStream output) throws IOException, WebApplicationException {
          byte[] buf = new byte[8192];
          int len = -1;
          while ((len = videoStream.read(buf)) != -1) {
            try {
              output.write(buf, 0, len);
            } catch (IOException e) {
              if (e.getMessage().toUpperCase().contains("RESET") || e.getMessage().toUpperCase().contains("BROKEN")) {
                // catched "Connection reset by peer" or "Broken pipe", which is
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
      .build();
  }

}
