/*-
 * #%L
 * Domain
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
package org.sing_group.piba.domain.entities.polyp;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static org.sing_group.fluent.checker.Checks.checkArgument;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.sing_group.piba.domain.entities.Identifiable;
import org.sing_group.piba.domain.entities.image.Gallery;

@Entity
@Table(name = "polypdataset")
public class PolypDataset implements Identifiable {
  @Id
  private String id;

  @Column(name = "creation_date", columnDefinition = "DATETIME(3)")
  private Timestamp creationDate;

  @Version
  @Column(name = "update_date", columnDefinition = "DATETIME(3)")
  private Timestamp updateDate;

  @Column(name = "title", nullable = false)
  private String title;
  
  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "polypDataset", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Set<PolypInDataset> polyps;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  private Gallery defaultGallery;
  
  PolypDataset() {}
  
  public PolypDataset(String title, String description, Collection<Polyp> polyps, Gallery defaultGallery) {
    this.id = UUID.randomUUID().toString();
    this.title = title;
    this.description = description;
    this.defaultGallery = defaultGallery;
    this.polyps = polyps.stream()
      .map(polyp -> new PolypInDataset(polyp, this))
    .collect(toSet());
    this.creationDate = this.updateDate = new Timestamp(System.currentTimeMillis());
  }

  @Override
  public String getId() {
    return this.id;
  }
  
  public String getTitle() {
    return title;
  }

  public void setTitle(String name) {
    this.title = checkArgument(name, n -> requireNonNull(n, "name cannot be null"));
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Stream<Polyp> getPolyps() {
    return this.polyps.stream()
      .map(PolypInDataset::getPolyp);
  }
  
  public void setPolyps(Collection<Polyp> polyps) {
    this.polyps.stream()
      .forEach(pid -> pid.getPolyp().removePolypInDataset(pid));
    this.polyps.clear();
    polyps.forEach(this::addPolyp);
  }
  
  public void addPolyp(Polyp polyp) {
    this.addPolypInDataset(new PolypInDataset(polyp, this));
  }
  
  public void removePolyp(Polyp polyp) {
    final PolypInDataset polypInDataset = this.polyps.stream()
      .filter(pid -> pid.getPolyp().equals(polyp))
      .findAny()
    .orElseThrow(() -> new IllegalArgumentException("Polyp does not belong to dataset"));
    
    this.removePolypInDataset(polypInDataset);
  }
  
  protected void addPolypInDataset(PolypInDataset polypInDataset) {
    if (this.polyps.add(polypInDataset)) {
      polypInDataset.getPolyp().addPolypInDataset(polypInDataset);
    }
  }
  
  protected void removePolypInDataset(PolypInDataset polypInDataset) {
    if (this.polyps.remove(polypInDataset)) {
      polypInDataset.getPolyp().removePolypInDataset(polypInDataset);
    }
  }
  
  public Gallery getDefaultGallery() {
    return defaultGallery;
  }
  
  public void setDefaultGallery(Gallery defaultGallery) {
    this.defaultGallery = defaultGallery;
  }
}
