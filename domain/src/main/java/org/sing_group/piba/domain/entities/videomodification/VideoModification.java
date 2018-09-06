package org.sing_group.piba.domain.entities.videomodification;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.sing_group.piba.domain.entities.VideoInterval;
import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.domain.entities.video.Video;

@Entity
@Table(name = "videomodification", uniqueConstraints = @UniqueConstraint(columnNames = {
  "video_id", "modifier_id", "start", "end"
}))
public class VideoModification extends VideoInterval implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;

  @ManyToOne
  private Video video;

  @ManyToOne
  private Modifier modifier;

  VideoModification() {}

  public VideoModification(Video video, Modifier modifier, Integer start, Integer end) {
    this.video = video;
    this.modifier = modifier;
    setStart(start);
    setEnd(end);
    checkInterval(start, end);
  }

  public int getId() {
    return id;
  }

  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

  public Modifier getModifier() {
    return modifier;
  }

  public void setModifier(Modifier modifier) {
    this.modifier = modifier;
  }

}
