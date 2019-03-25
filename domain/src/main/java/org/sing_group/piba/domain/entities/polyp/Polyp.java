/*-
 * #%L
 * Domain
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
package org.sing_group.piba.domain.entities.polyp;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.checkArgument;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.Identifiable;
import org.sing_group.piba.domain.entities.exploration.Exploration;
import org.sing_group.piba.domain.entities.polyprecording.PolypRecording;

@Entity
@Table(name = "polyp")
public class Polyp implements Identifiable {

  @Id
  private String id;
  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "size")
  private Integer size;
  @Enumerated(EnumType.STRING)
  @Column(name = "location")
  private Location location;
  @Enumerated(EnumType.STRING)
  @Column(name = "wasp")
  private WASP wasp;
  @Enumerated(EnumType.STRING)
  @Column(name = "nice")
  private NICE nice;
  @Enumerated(EnumType.STRING)
  @Column(name = "lst")
  private LST lst;
  @Enumerated(EnumType.STRING)
  @Column(name = "paris_primary")
  private PARIS parisPrimary;
  @Enumerated(EnumType.STRING)
  @Column(name = "paris_secondary")
  private PARIS parisSecondary;

  @Column(name = "observation")
  private String observation;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  private PolypHistology histology;

  @ManyToOne
  private Exploration exploration;

  @OneToMany(mappedBy = "polyp", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<PolypRecording> polypRecordings = new HashSet<>();

  Polyp() {}

  public Polyp(
    String name, Integer size, Location location, WASP wasp, NICE nice, LST lst, PARIS parisPrimary,
    PARIS parisSecondary, PolypHistology histology, String observation, Exploration exploration
  ) {
    this.id = UUID.randomUUID().toString();
    setName(name);
    this.size = size;
    this.location = location;
    this.wasp = wasp;
    this.nice = nice;
    this.lst = lst;
    this.parisPrimary = parisPrimary;
    this.parisSecondary = parisSecondary;
    this.histology = histology;
    this.observation = observation;
    setExploration(exploration);
  }

  @Override
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    checkArgument(name, n -> requireNonNull(n, "name cannot be null"));
    this.name = name;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public WASP getWasp() {
    return wasp;
  }

  public void setWasp(WASP wasp) {
    this.wasp = wasp;
  }

  public NICE getNice() {
    return nice;
  }

  public void setNice(NICE nice) {
    this.nice = nice;
  }

  public LST getLst() {
    return lst;
  }

  public void setLst(LST lst) {
    this.lst = lst;
  }

  public PARIS getParisPrimary() {
    return parisPrimary;
  }

  public void setParisPrimary(PARIS parisPrimary) {
    this.parisPrimary = parisPrimary;
  }

  public PARIS getParisSecondary() {
    return parisSecondary;
  }

  public void setParisSecondary(PARIS parisSecondary) {
    this.parisSecondary = parisSecondary;
  }

  public PolypHistology getHistology() {
    return histology;
  }

  public void setHistology(PolypHistology histology) {
    this.histology = histology;
  }

  public String getObservation() {
    return observation;
  }

  public void setObservation(String observation) {
    this.observation = observation;
  }

  public Exploration getExploration() {
    return exploration;
  }

  public void setExploration(Exploration exploration) {
    if (this.exploration != null) {
      this.exploration.internalRemovePolyp(this);
    }
    this.exploration = exploration;
    if (exploration != null) {
      this.exploration.internalAddPolyp(this);
    }
  }
}
