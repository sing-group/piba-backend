/*-
 * #%L
 * Domain
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
package org.sing_group.piba.domain.entities.image;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.checkArgument;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.Identifiable;

@Entity
@Table(name = "gallery")
public class Gallery implements Identifiable {

  @Id
  private String id;
  @Column(name = "title", nullable = false, unique = true)
  private String title;
  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "gallery", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Image> images = new ArrayList<>();

  Gallery() {}

  public Gallery(String name, String description) {
    this.id = UUID.randomUUID().toString();
    setTitle(name);
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    checkArgument(title, t -> requireNonNull(t, "title cannot be null"));
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getId() {
    return id;
  }

  public List<Image> getImages() {
    return images;
  }

  public void internalRemoveImage(Image image) {
    this.images.remove(image);
  }

  public void internalAddImage(Image image) {
    this.images.add(image);
  }
}
