package org.junit.tools.generator.utils.testmodel;

public class ExceptionWithoutDefaultContstructor extends Exception {

  private static final long serialVersionUID = -2346962819894025488L;

  public ExceptionWithoutDefaultContstructor(String message, Throwable cause) {
    super(message, cause);
  }

  public ExceptionWithoutDefaultContstructor(Throwable cause) {
    super(cause);
  }

}
