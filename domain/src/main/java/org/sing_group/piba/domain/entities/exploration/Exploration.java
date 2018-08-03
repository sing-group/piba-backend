package org.sing_group.piba.domain.entities.exploration;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.polyp.Polyp;
import org.sing_group.piba.domain.entities.video.Video;

@Entity
@Table(name = "exploration")
public class Exploration {
  @Id
  private String id;

  @Column(name = "location", nullable = false)
  private String location;

  @Column(name = "date", nullable = false)
  private Date date;

  @OneToMany(mappedBy = "exploration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Video> videos = new ArrayList<>();

  @OneToMany(mappedBy = "exploration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Polyp> polyps = new ArrayList<>();

  Exploration() {}

  public Exploration(String location, Date date) {
    id = UUID.randomUUID().toString();
    this.setLocation(location);
    this.setDate(date);
  }

  public String getId() {
    return id;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    requireNonNull(location);
    this.location = location;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    requireNonNull(date);
    this.date = date;
  }

  public void addVideo(Video video) {
    video.setExploration(this);
  }

  public void internalRemoveVideo(Video video) {
    this.videos.remove(video);
  }

  public void internalAddVideo(Video video) {
    this.videos.add(video);
  }

  public List<Video> getVideos() {
    return videos;
  }

  public void addPolyp(Polyp polyp) {
    polyp.setExploration(this);
  }

  public void internalRemovePolyp(Polyp polyp) {
    this.polyps.remove(polyp);
  }

  public void internalAddPolyp(Polyp polyp) {
    this.polyps.add(polyp);
  }

  public List<Polyp> getPolyps() {
    return polyps;
  }

}
