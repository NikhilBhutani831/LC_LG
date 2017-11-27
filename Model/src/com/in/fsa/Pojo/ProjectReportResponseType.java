package com.in.fsa.Pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "G_1", propOrder = {
    "value",
    "description"
})

public class ProjectReportResponseType {
@XmlElement(required = true, nillable = true,name = "VALUE")
private String value;
@XmlElement(required = true, nillable = true,name = "DESCRIPTION")
private String description;

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
