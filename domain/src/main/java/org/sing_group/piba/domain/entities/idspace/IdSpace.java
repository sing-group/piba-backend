package org.sing_group.piba.domain.entities.idspace;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sing_group.piba.domain.entities.Identifiable;

@Entity
@Table(name = "idspace")
public class IdSpace implements Identifiable {

  @Id
  @Column(name = "id")
  private String id;
  @Column(name = "name", nullable = false)
  private String name;

  IdSpace() {}

  public IdSpace(String name) {
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
