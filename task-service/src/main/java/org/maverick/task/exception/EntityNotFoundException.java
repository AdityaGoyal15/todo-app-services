package org.maverick.task.exception;

public class EntityNotFoundException extends ToDoAppException {

  public EntityNotFoundException(String code, String message) {
    super(code, message);
  }
}
