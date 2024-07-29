package org.maverick.task;

import java.util.List;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.UpdateTaskDto;

public interface TaskService {

  List<Task> findAllByUserId(Long userId);

  Task save(CreateTaskDto requestDto, Long userId);

  Task update(Long id, UpdateTaskDto requestDto);

  Task delete(Long id);
}
