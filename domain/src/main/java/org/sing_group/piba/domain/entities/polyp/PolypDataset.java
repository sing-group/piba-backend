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
import static org.sing_group.fluent.checker.Checks.checkArgument;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.sing_group.piba.domain.entities.Identifiable;

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

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
    name = "polypsindataset",
    joinColumns = @JoinColumn(name = "polypdataset_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "polyp_id", referencedColumnName = "id")
  )
  private Set<Polyp> polyps;
  
  PolypDataset() {}
  
  public PolypDataset(String title) {
    this.id = UUID.randomUUID().toString();
    this.title = title;
    this.polyps = new HashSet<>();
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
  
  public Set<Polyp> getPolyps() {
    return polyps;
  }
  
  public void addPolyp(Polyp polyp) {
    this.polyps.add(polyp);
  }
}
