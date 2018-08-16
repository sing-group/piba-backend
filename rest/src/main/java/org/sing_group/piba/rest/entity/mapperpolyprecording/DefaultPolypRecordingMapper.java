package org.sing_group.piba.rest.entity.mapperpolyprecording;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.mapper.spi.polyprecording.PolypRecordingMapper;
import org.sing_group.piba.rest.entity.polyptrecording.PolypRecordingData;
import org.sing_group.piba.rest.resource.polyp.DefaultPolypResource;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;

@Default
public class DefaultPolypRecordingMapper implements PolypRecordingMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public PolypRecordingData toPolypRecordingData(PolypRecording polypRecording) {
    return new PolypRecordingData(
      UuidAndUri.fromEntity(requestURI, polypRecording.getVideo(), DefaultVideoResource.class),
      UuidAndUri.fromEntity(requestURI, polypRecording.getPolyp(), DefaultPolypResource.class),
      polypRecording.getStart(), polypRecording.getEnd()
    );
  }

}
