package org.maverick.task.response;

import java.util.List;
import org.maverick.task.exception.ToDoAppException;

public record APIResponse<T>(T data, List<ToDoAppException> errors, boolean success) {}
