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
public class DummyClassThrowingException {
  @XmlElement(name = "name")
  private final String name;
  @XmlAttribute
  private final Integer number;

  public DummyClassThrowingException() throws IllegalAccessException {
    name = "default";
    number = 0;
    throw new IllegalAccessException("Do not use default constructor!");
  }

  public DummyClassThrowingException(String name, Integer num) {
    this.name = name;
    number = num;
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
}
