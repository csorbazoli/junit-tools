package org.junit.tools.generator.utils.testmodel;

public enum DummyEnum {
  VALUE_8("8"),
  VALUE_12("12"),
  VALUE_3("3"),
  VALUE_7("7"),
  ;

  private final String value;

  DummyEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

}
