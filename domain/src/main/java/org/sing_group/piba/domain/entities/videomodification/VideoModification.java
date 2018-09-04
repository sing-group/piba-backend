package org.sing_group.piba.domain.entities.videomodification;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.VideoInterval;
import org.sing_group.piba.domain.entities.modifier.Modifier;
import org.sing_group.piba.domain.entities.video.Video;
import org.sing_group.piba.domain.entities.videomodification.VideoModification.VideoModificationId;

@Entity
@Table(name = "videomodification")
@IdClass(VideoModificationId.class)
public class VideoModification extends VideoInterval implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @ManyToOne
  private Video video;

  @Id
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

  public static class VideoModificationId implements Serializable {
    private static final long serialVersionUID = 1L;

    private String video;
    private String modifier;

    public VideoModificationId() {}

    public VideoModificationId(String video, String modifier) {
      this.video = video;
      this.modifier = modifier;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((modifier == null) ? 0 : modifier.hashCode());
      result = prime * result + ((video == null) ? 0 : video.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      VideoModificationId other = (VideoModificationId) obj;
      if (modifier == null) {
        if (other.modifier != null)
          return false;
      } else if (!modifier.equals(other.modifier))
        return false;
      if (video == null) {
        if (other.video != null)
          return false;
      } else if (!video.equals(other.video))
        return false;
      return true;
    }

  }

}
