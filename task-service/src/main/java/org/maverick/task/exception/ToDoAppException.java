package org.maverick.task.exception;

public class ToDoAppException extends RuntimeException {
  private final String code;

  public ToDoAppException(String code, String message) {
    super(message, null, false, false);
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
