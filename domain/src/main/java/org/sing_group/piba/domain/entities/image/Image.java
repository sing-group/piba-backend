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

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.sing_group.piba.domain.entities.Identifiable;
import org.sing_group.piba.domain.entities.video.Video;

@Entity
@Table(name = "image", uniqueConstraints = @UniqueConstraint(columnNames = {
  "gallery_id", "video_id", "numFrame"
}))
public class Image implements Identifiable {

  @Id
  private String id;

  @Column(name = "numFrame")
  private int numFrame;

  @Column(name = "is_removed")
  private boolean isRemoved = false;

  @ManyToOne
  private Gallery gallery;

  @ManyToOne
  private Video video;

  Image() {}

  public Image(int numFrame, boolean isRemoved, Gallery gallery, Video video) {
    this.id = UUID.randomUUID().toString();
    this.numFrame = numFrame;
    this.isRemoved = isRemoved;
    setGallery(gallery);
    setVideo(video);
  }

  @Override
  public String getId() {
    return id;
  }

  public int getNumFrame() {
    return numFrame;
  }

  public void setNumFrame(int numFrame) {
    this.numFrame = numFrame;
  }

  public boolean isRemoved() {
    return isRemoved;
  }

  public void setRemoved(boolean isRemoved) {
    this.isRemoved = isRemoved;
  }

  public Gallery getGallery() {
    return gallery;
  }

  public void setGallery(Gallery gallery) {
    if (this.gallery != null) {
      this.gallery.internalRemoveImage(this);
    }
    this.gallery = gallery;
    if (gallery != null) {
      this.gallery.internalAddImage(this);
    }
  }

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

}