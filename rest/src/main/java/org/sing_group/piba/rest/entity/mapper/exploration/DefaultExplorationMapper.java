package org.sing_group.piba.rest.entity.mapper.exploration;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Default;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.exploration.ExplorationData;
import org.sing_group.piba.rest.entity.mapper.spi.exploration.ExplorationMapper;
import org.sing_group.piba.rest.resource.video.DefaultVideoResource;

@Default
public class DefaultExplorationMapper implements ExplorationMapper {

  private UriInfo requestURI;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public ExplorationData toExplorationData(Exploration exploration) {
    return new ExplorationData(
      exploration.getId(), exploration.getLocation(), exploration.getDate(), linkVideos(exploration.getVideos())
    );
  }

  private List<UuidAndUri> linkVideos(List<Video> videos) {
    List<UuidAndUri> urls = new ArrayList<>();
    for (Video video : videos) {
        urls.add(
          new UuidAndUri(
            video.getId(),
            requestURI.getBaseUriBuilder().path(
              UriBuilder.fromResource(DefaultVideoResource.class).path(video.getId()).build().toString())
            .build()
          )
        );

    }
    return urls;
  }
}
