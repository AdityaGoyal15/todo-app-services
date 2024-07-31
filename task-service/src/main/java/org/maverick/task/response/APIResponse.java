package org.maverick.task.response;

import java.util.List;

import org.maverick.task.exception.ToDoAppException;
import org.springframework.http.HttpStatus;

public record APIResponse<T>(T data, List<ToDoAppException> errors, HttpStatus status, int statusCode) {}
