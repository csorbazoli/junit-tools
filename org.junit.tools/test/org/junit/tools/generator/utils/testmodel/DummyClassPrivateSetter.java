package org.junit.tools.generator.utils.testmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DummyClass", propOrder = { "name", "number" })
public class DummyClassPrivateSetter {
  @XmlElement(name = "name")
  private String name;
  @XmlAttribute
  private Integer number;

  public DummyClassPrivateSetter() {
    setName(null);
  }

  public DummyClassPrivateSetter(String name, Integer num) {
    setName(name);
    setNumber(num);
  }

  public String getName() {
    return name;
  }

  public Integer getNumber() {
    return number;
  }

  @Override
  public String toString() {
    return "name: " + name + ", number: " + number;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void setNumber(Integer number) {
    this.number = number;
  }
}
