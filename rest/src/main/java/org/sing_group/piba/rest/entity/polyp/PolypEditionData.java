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
package org.sing_group.piba.rest.entity.polyp;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.piba.domain.entities.polyp.LST;
import org.sing_group.piba.domain.entities.polyp.Location;
import org.sing_group.piba.domain.entities.polyp.NICE;
import org.sing_group.piba.domain.entities.polyp.PARIS;
import org.sing_group.piba.domain.entities.polyp.WASP;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "polyp-edition-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "polyp-edition-data", description = "Information of a polyp for edition.")
public class PolypEditionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id", required = true)
  private String id;
  @XmlElement(name = "name")
  private String name;
  @XmlElement(name = "size")
  private Integer size;
  @XmlElement(name = "location")
  private Location location;
  @XmlElement(name = "wasp")
  private WASP wasp;
  @XmlElement(name = "nice")
  private NICE nice;
  @XmlElement(name = "lst")
  private LST lst;
  @XmlElement(name = "parisPrimary")
  private PARIS parisPrimary;
  @XmlElement(name = "parisSecondary")
  private PARIS parisSecondary;
  @XmlElement(name = "histology")
  private String histology;
  @XmlElement(name = "observation")
  private String observation;

  @XmlElement(name = "exploration")
  private String exploration;

  PolypEditionData() {}

  public PolypEditionData(
    String id, String name, Integer size, Location location, WASP wasp, NICE nice, LST lst,
    PARIS parisPrimary, PARIS parisSecondary, String histology, String observation, String exploration
  ) {
    this.id = id;
    this.name = name;
    this.size = size;
    this.location = location;
    this.wasp = wasp;
    this.nice = nice;
    this.lst = lst;
    this.parisPrimary = parisPrimary;
    this.parisSecondary = parisSecondary;
    this.histology = histology;
    this.observation = observation;
    this.exploration = exploration;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getSize() {
    return size;
  }

  public Location getLocation() {
    return location;
  }

  public WASP getWasp() {
    return wasp;
  }

  public NICE getNice() {
    return nice;
  }

  public LST getLst() {
    return lst;
  }

  public PARIS getParisPrimary() {
    return parisPrimary;
  }

  public PARIS getParisSecondary() {
    return parisSecondary;
  }

  public String getHistology() {
    return histology;
  }

  public String getObservation() {
    return observation;
  }

  public String getExploration() {
    return exploration;
  }
}
