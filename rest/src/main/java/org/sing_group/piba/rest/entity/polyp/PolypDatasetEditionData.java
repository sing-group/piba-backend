/*-
 * #%L
 * REST
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato,
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
package org.sing_group.piba.rest.entity.polyp;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "polyp-dataset-edition-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "polyp-dataset-edition-data", description = "A set of related polyps for edition.")
public class PolypDatasetEditionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "title", required = true)
  private String title;

  @XmlElement(name = "polyps")
  private List<String> polyps;
  
  @XmlElement(name = "defaultGallery")
  private String defaultGallery;
  
  PolypDatasetEditionData() {}


  public String getTitle() {
    return title;
  }

  public void setTitle(String name) {
    this.title = name;
  }

  public List<String> getPolyps() {
    return polyps;
  }

  public void setPolyps(List<String> polyps) {
    this.polyps = polyps;
  }

  public String getDefaultGallery() {
    return defaultGallery;
  }

  public void setDefaultGallery(String defaultGallery) {
    this.defaultGallery = defaultGallery;
  }
}
