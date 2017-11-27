package com.in.fsa.Pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DATA_DS", propOrder = {
    "G_1"
})
@XmlRootElement(name = "DATA_DS")
public class DATA_DS {
  @XmlElement(required = true, nillable = true)
  private List<G_1> G_1;

      public List<G_1> getG_1 ()
      {
          return G_1;
      }

      public void setG_1 (List<G_1> G_1)
      {
          this.G_1 = G_1;
      }
}
