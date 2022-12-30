package org.junit.tools.generator.utils.testmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TestValue", propOrder = { "typeName", "value" })
public class TestValue {
  @XmlElement(name = "typeName")
  private String typeName;
  @XmlAttribute
  private Object value;

  public TestValue() {
    // default constructor
  }

  public TestValue(Object value) {
    typeName = value.getClass().getName();
    this.value = value;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String name) {
    typeName = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object v) {
    value = v;
  }

  @Override
  public String toString() {
    return "typeName: " + typeName + ", value: " + value;
  }
}
