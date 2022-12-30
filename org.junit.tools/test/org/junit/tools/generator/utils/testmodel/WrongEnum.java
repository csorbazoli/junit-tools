package org.junit.tools.generator.utils.testmodel;

public class WrongEnum {

  private final String value;

  public WrongEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  public WrongEnum[] values() {
    throw new IllegalArgumentException("TEST EXCEPTION");
  }
}
