package org.maverick.task.implementation.database;

import java.util.List;
import java.util.Optional;

import org.maverick.task.Task;
import org.maverick.task.TaskRepository;
import org.maverick.task.exception.EntityNotFoundException;
import org.maverick.task.service.database.TaskReadDatabaseService;
import org.springframework.stereotype.Service;

@Service
public class TaskReadDatabaseServiceImpl implements TaskReadDatabaseService {

  private final TaskRepository taskRepository;

  public TaskReadDatabaseServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public List<Task> findAllByUserId(Long userId) {
    return taskRepository.findAllByUserId(userId);
  }

  @Override
  public Optional<Task> findByTitleAndUserId(String title, Long userId) {
    return taskRepository.findByTitleAndUserId(title, userId);
  }

  @Override
  public Task findByIdOrElseThrow(Long id) {
    return taskRepository
            .findById(id)
            .orElseThrow(
                    () -> new EntityNotFoundException("Task with ID [%d] not found".formatted(id)));
  }
}
