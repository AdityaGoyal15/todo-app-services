package org.maverick.task.service;

import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.TaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.maverick.task.dto.UserTasksDto;
import org.maverick.task.response.APIResponse;

public interface TaskService {

  APIResponse<UserTasksDto> findAllByUserId(Long userId);

  APIResponse<TaskDto> save(CreateTaskDto requestDto, Long userId);

  APIResponse<TaskDto> update(Long id, UpdateTaskDto requestDto);

  APIResponse<TaskDto> delete(Long id);
}
