package org.sing_group.piba.rest.entity.idspace;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "IDSpace-edition-data", namespace = "http://entity.resource.rest.piba.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "IDSpace-edition-data", description = "Information of a ID Space for edition.")
public class IdSpaceEditionData implements Serializable {
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "id")
  private String id;

  @XmlElement(name = "name", required = true)
  private String name;

  public IdSpaceEditionData() {}

  public IdSpaceEditionData(String id, String name) {
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
