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
package org.sing_group.piba.rest.entity.mapper;

import static java.util.stream.Collectors.toList;

import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.polyp.PolypDataset;
import org.sing_group.piba.rest.entity.UuidAndUri;
import org.sing_group.piba.rest.entity.mapper.spi.PolypDatasetMapper;
import org.sing_group.piba.rest.entity.polyp.PolypDatasetData;
import org.sing_group.piba.rest.entity.polyp.PolypDatasetEditionData;
import org.sing_group.piba.rest.resource.image.DefaultGalleryResource;
import org.sing_group.piba.rest.resource.polyp.DefaultPolypDatasetResource;
import org.sing_group.piba.service.spi.image.GalleryService;
import org.sing_group.piba.service.spi.polyp.PolypService;

@Default
public class DefaultPolypDatasetMapper implements PolypDatasetMapper {

  private UriInfo requestURI;
  
  @Inject
  private PolypService polypService;
  
  @Inject
  private GalleryService galleryService;

  @Override
  public void setRequestURI(UriInfo requestURI) {
    this.requestURI = requestURI;
  }

  @Override
  public PolypDatasetData toPolypDatasetData(PolypDataset dataset) {
    return new PolypDatasetData(
      dataset.getId(), dataset.getTitle(),
      UuidAndUri.fromEntities(requestURI, dataset.getPolyps().collect(toList()), DefaultPolypDatasetResource.class),
      UuidAndUri.fromEntity(requestURI, dataset.getDefaultGallery(), DefaultGalleryResource.class)
    );
  }
  
  @Override
  public void assignPolypDatasetEditionData(PolypDataset polypDataset, PolypDatasetEditionData data) {
    polypDataset.setTitle(data.getTitle());
    if (data.getDefaultGallery() == null) {
      polypDataset.setDefaultGallery(null);
    } else {
      polypDataset.setDefaultGallery(this.galleryService.get(data.getDefaultGallery()));
    }
    
    final Set<Polyp> polyps = data.getPolyps().stream()
      .map(this.polypService::getPolyp)
    .collect(Collectors.toSet());
    polypDataset.setPolyps(polyps);
  }
}
