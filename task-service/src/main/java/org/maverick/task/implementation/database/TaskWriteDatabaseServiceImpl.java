package org.maverick.task.implementation.database;

import jakarta.transaction.Transactional;
import org.maverick.task.Task;
import org.maverick.task.TaskRepository;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.maverick.task.exception.EntityNotFoundException;
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
  public Task save(CreateTaskDto createTaskDto, Long userId) {
    Task task = new Task();
    task.setTitle(createTaskDto.title());
    task.setDescription(createTaskDto.description());
    task.setStatus("To Do");
    task.setUserId(userId);
    return taskRepository.save(task);
  }

  @Override
  @Transactional
  public Task update(Long id, UpdateTaskDto updateTaskDto) {
    Task task = findByIdOrElseThrow(id);
    task.setTitle(updateTaskDto.title());
    task.setDescription(updateTaskDto.description());
    task.setStatus(updateTaskDto.status());
    return taskRepository.save(task);
  }

  @Override
  @Transactional
  public Task delete(Long id) {
    Task task = findByIdOrElseThrow(id);
    taskRepository.deleteById(id);
    return task;
  }

  private Task findByIdOrElseThrow(Long id) {
    return taskRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException("Task with ID [%d] not found".formatted(id)));
  }
}
