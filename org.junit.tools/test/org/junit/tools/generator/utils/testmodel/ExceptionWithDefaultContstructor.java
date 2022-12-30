package org.junit.tools.generator.utils.testmodel;

public class ExceptionWithDefaultContstructor extends Exception {

  private static final long serialVersionUID = -2346962819894025488L;

  public ExceptionWithDefaultContstructor() {
    super();
  }

  public ExceptionWithDefaultContstructor(String message, Throwable cause) {
    super(message, cause);
  }

  public ExceptionWithDefaultContstructor(Throwable cause) {
    super(cause);
  }

}
