package org.sing_group.piba.rest.entity.modifier;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "Modifier-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "Modifier-data", description = "Information of a modifier.")
public class ModifierData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id", required = true)
  private String id;

  @XmlElement(name = "name", required = true)
  private String name;

  ModifierData() {}

  public ModifierData(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}
