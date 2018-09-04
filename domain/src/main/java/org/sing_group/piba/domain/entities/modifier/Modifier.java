package org.sing_group.piba.domain.entities.modifier;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.Identifiable;

@Entity
@Table(name = "modifier")
public class Modifier implements Identifiable {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "name", nullable = false)
  private String name;

  Modifier() {}

  public Modifier(String name) {
    this.id = UUID.randomUUID().toString();
    setName(name);
  }

  @Override
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    requireNonNull(name);
    this.name = name;
  }

}
