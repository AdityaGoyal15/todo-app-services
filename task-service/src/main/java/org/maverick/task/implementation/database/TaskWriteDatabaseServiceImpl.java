package org.maverick.task.implementation.database;

import jakarta.transaction.Transactional;
import org.maverick.task.Task;
import org.maverick.task.TaskRepository;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.maverick.task.service.database.TaskWriteDatabaseService;
import org.springframework.stereotype.Service;

@Service
public class TaskWriteDatabaseServiceImpl implements TaskWriteDatabaseService {

  private final TaskRepository taskRepository;

  public TaskWriteDatabaseServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  @Transactional
  public Task save(CreateTaskDto dto, Long userId) {
    Task task = new Task(dto, userId);
    return taskRepository.save(task);
  }

  @Override
  @Transactional
  public Task update(Task task, UpdateTaskDto dto) {
    task.update(dto);
    return taskRepository.save(task);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    taskRepository.deleteById(id);
  }
}
