package org.junit.tools.generator.utils.testmodel;

public enum EmptyEnum {
  ;

  private final String value;

  EmptyEnum(String value) {
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
