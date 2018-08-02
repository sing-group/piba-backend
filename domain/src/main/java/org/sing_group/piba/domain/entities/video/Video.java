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
package org.sing_group.piba.domain.entities.video;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.Identifiable;
import org.sing_group.piba.domain.entities.exploration.Exploration;

@Entity
@Table(name = "video")
public class Video implements Identifiable {
  @Id
  private String id;

  @Column(name = "title")
  private String title;

  @Column(name = "observations", length = 3000)
  private String observations;

  @Column(name = "is_processing")
  private boolean isProcessing = true;

  @ManyToOne
  @JoinColumn(name = "exploration_id")
  private Exploration exploration;

  public Video() {
    id = UUID.randomUUID().toString();
  }

  @Override
  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getObservations() {
    return observations;
  }

  public void setObservations(String observations) {
    this.observations = observations;
  }

  public void setProcessing(boolean isProcessing) {
    this.isProcessing = isProcessing;
  }

  public boolean isProcessing() {
    return isProcessing;
  }

  public Exploration getExploration() {
    return exploration;
  }

  public void setExploration(Exploration exploration) {
    if (this.exploration != null) {
      this.exploration.internalRemoveVideo(this);
    }
    this.exploration = exploration;
    if (exploration != null) {
      this.exploration.internalAddVideo(this);
    }
  }
}
