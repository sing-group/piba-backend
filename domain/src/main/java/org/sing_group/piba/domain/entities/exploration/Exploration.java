package org.sing_group.piba.domain.entities.exploration;

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

  public Exploration() {
    id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
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

}
