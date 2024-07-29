package org.maverick.task.service.database;

import org.maverick.task.Task;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.UpdateTaskDto;

public interface TaskWriteDatabaseService {

  Task save(CreateTaskDto createTaskDto, Long userId);

  Task update(Task task, UpdateTaskDto updateTaskDto);

  void delete(Long id);
}
