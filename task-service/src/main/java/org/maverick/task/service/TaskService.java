package org.maverick.task.service;

import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.TaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.maverick.task.dto.UserTasksDto;

public interface TaskService {

  UserTasksDto findAllByUserId(Long userId);

  TaskDto save(CreateTaskDto requestDto, Long userId);

  TaskDto update(Long id, UpdateTaskDto requestDto);

  TaskDto delete(Long id);
}
