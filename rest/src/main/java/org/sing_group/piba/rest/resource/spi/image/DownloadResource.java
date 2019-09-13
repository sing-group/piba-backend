package org.sing_group.piba.rest.resource.spi.image;

import java.io.FileNotFoundException;

import javax.ws.rs.core.Response;

public interface DownloadResource {

  public Response getGalleryInZip(String id, String filter, Boolean withLocation) throws FileNotFoundException;

}
