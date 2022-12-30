package org.junit.tools.generator.utils.testmodel;

public class ExceptionWithDefaultContstructorThrowingException extends Exception {

  private static final long serialVersionUID = -2346962819894025488L;

  public ExceptionWithDefaultContstructorThrowingException() throws Exception {
    throw new IllegalAccessException("Do not use default constructor!");
  }

  public ExceptionWithDefaultContstructorThrowingException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExceptionWithDefaultContstructorThrowingException(Throwable cause) {
    super(cause);
  }

}
