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
package org.sing_group.piba.rest.entity.exploration;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.rest.entity.UuidAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "exploration-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "exploration-data", description = "Information of a exploration.")
public class ExplorationData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id", required = true)
  private String id;

  @XmlElement(name = "title", required = true)
  private String title;

  @XmlElement(name = "location", required = true)
  private String location;

  @XmlElement(name = "explorationDate", required = true)
  private Date exporationDate;

  @XmlElement(name = "videos")
  private List<UuidAndUri> videos;

  @XmlElement(name = "polyps")
  private List<UuidAndUri> polyps;

  @XmlElement(name = "numPolyps")
  private int numPolyps;

  @XmlElement(name = "numVideos")
  private int numVideos;
  
  @XmlElement(name = "confirmed")
  private boolean confirmed;

  @XmlElement(name = "patient")
  private UuidAndUri patient;

  public ExplorationData() {}

  public ExplorationData(
    String id, String title, String location, Date explorationDate, List<UuidAndUri> videos, List<UuidAndUri> polyps,
    UuidAndUri patient, int numVideos, int numPolyps, boolean confirmed 
  ) {
    this.id = id;
    this.title = title;
    this.location = location;
    this.exporationDate = explorationDate;
    this.videos = videos;
    this.polyps = polyps;
    this.patient = patient;
    this.numPolyps = numPolyps;
    this.numVideos = numVideos;
    this.confirmed = confirmed;
  }

  public String getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getExplorationDate() {
    return this.exporationDate;
  }

  public void setExplorationDate(Date explorationDate) {
    this.exporationDate = explorationDate;
  }

  public List<UuidAndUri> getVideos() {
    return this.videos;
  }

  public List<UuidAndUri> getPolyps() {
    return this.polyps;
  }

  public UuidAndUri getPatient() {
    return this.patient;
  }

  public int getNumVideos() {
    return this.numVideos;
  }

  public int getNumPolyps() {
    return this.numPolyps;
  }
  
  public boolean isConfirmed() {
    return this.confirmed;
  }

  public void setConfirmed(boolean confirmed) {
    this.confirmed = confirmed;
  }

}
