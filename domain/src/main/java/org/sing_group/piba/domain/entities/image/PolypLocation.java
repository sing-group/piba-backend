/*-
 * #%L
 * Domain
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato,
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

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.Identifiable;

@Entity
@Table(name = "polyplocation")
public class PolypLocation implements Identifiable {

  @Id
  private String id;

  @Column(name = "x", nullable = false)
  private Integer x;

  @Column(name = "y", nullable = false)
  private Integer y;

  @Column(name = "width", nullable = false)
  private Integer width;

  @Column(name = "height", nullable = false)
  private Integer height;

  @Column(name = "creation_date", columnDefinition = "DATETIME(3)")
  private Timestamp creationDate;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Image image;

  public PolypLocation() {}

  public PolypLocation(Integer x, Integer y, Integer width, Integer height, Image image) {
    this.id = UUID.randomUUID().toString();
    this.creationDate = new Timestamp(System.currentTimeMillis());
    this.setX(x);
    this.setY(y);
    this.setWidth(width);
    this.setHeight(height);
    this.setImage(image);
  }

  @Override
  public String getId() {
    return id;
  }

  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    checkArgument(x, coordinate -> requireNonNull(coordinate, "X can not be null"));
    this.x = x;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    checkArgument(y, coordinate -> requireNonNull(coordinate, "Y can not be null"));
    this.y = y;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    checkArgument(width, w -> requireNonNull(w, "Width can not be null"));
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    checkArgument(height, h -> requireNonNull(h, "Height can not be null"));
    this.height = height;
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    if (image == null) {
      if (this.image != null) {
        this.image.setPolypLocation(null);
      }
    } else {
      image.setPolypLocation(this);
    }
    this.image = image;
  }

}
