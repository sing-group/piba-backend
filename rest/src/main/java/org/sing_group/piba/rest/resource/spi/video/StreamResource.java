package org.sing_group.piba.rest.resource.spi.video;

import javax.ws.rs.core.Response;

public interface StreamResource {
  public Response getVideoStream(String id, String format);
}
