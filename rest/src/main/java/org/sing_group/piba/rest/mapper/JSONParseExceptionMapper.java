package org.sing_group.piba.rest.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@Provider
public class JSONParseExceptionMapper implements ExceptionMapper<InvalidFormatException> {
  @Override
  public Response toResponse(final InvalidFormatException jpe) {
    return Response.status(Status.BAD_REQUEST)
      .entity(
        "Invalid data supplied for request. Cannot assign \"" + jpe.getValue() + "\" to a field of type "
          + jpe.getTargetType().getSimpleName()
      ).build();
  }
}