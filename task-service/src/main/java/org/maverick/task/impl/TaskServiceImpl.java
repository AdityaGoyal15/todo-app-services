package org.maverick.task.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import org.maverick.task.Task;
import org.maverick.task.TaskRepository;
import org.maverick.task.TaskService;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.maverick.task.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  public TaskServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public List<Task> findAllByUserId(Long userId) {
    return taskRepository.findAllByUserId(userId);
  }

  @Override
  @Transactional
  public Task save(CreateTaskDto requestDto, Long userId) {
    String title = requestDto.title();
    Task task = findByUserIdAndTitleOrElseNull(userId, title);

    if (task != null) {
      throw new IllegalArgumentException(
          "The User with ID [%d] has already created a Task with title [%s]"
              .formatted(userId, title));
    }

    task = new Task();
    task.setTitle(title);
    task.setDescription(requestDto.description());
    task.setStatus("TODO");
    task.setUserId(userId);
    return taskRepository.save(task);
  }

  private Task findByUserIdAndTitleOrElseNull(Long userId, String title) {
    return taskRepository.findByTitleAndUserId(title, userId).orElse(null);
  }

  @Override
  @Transactional
  public Task update(Long id, UpdateTaskDto requestDto) {
    Task task = findByIdOrElseThrow(id);
    task.setTitle(requestDto.title());
    task.setDescription(requestDto.description());
    task.setStatus(requestDto.status());
    return taskRepository.save(task);
  }

  private Task findByIdOrElseThrow(Long id) {
    return taskRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException("Task with ID [%d] not found".formatted(id)));
  }

  @Override
  @Transactional
  public Task delete(Long id) {
    Task task = findByIdOrElseThrow(id);
    taskRepository.deleteById(id);
    return task;
  }
}
