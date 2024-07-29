package org.maverick.task.dto;

import java.util.List;
import org.maverick.task.Task;
import org.maverick.task.external.User;

public record UserTasksDto(List<Task> tasks, User user) {}
