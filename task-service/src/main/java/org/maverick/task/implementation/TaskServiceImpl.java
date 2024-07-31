package org.maverick.task.implementation;

import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.maverick.task.Task;
import org.maverick.task.client.UserClient;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.TaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.maverick.task.dto.UserTasksDto;
import org.maverick.task.exception.ToDoAppException;
import org.maverick.task.external.User;
import org.maverick.task.response.APIResponse;
import org.maverick.task.service.TaskService;
import org.maverick.task.service.database.TaskReadDatabaseService;
import org.maverick.task.service.database.TaskWriteDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskReadDatabaseService taskReadDatabaseService;
  private final TaskWriteDatabaseService taskWriteDatabaseService;
  private final UserClient userClient;

  public TaskServiceImpl(
      TaskReadDatabaseService taskReadDatabaseService,
      TaskWriteDatabaseService taskWriteDatabaseService,
      UserClient userClient) {
    this.taskReadDatabaseService = taskReadDatabaseService;
    this.taskWriteDatabaseService = taskWriteDatabaseService;
    this.userClient = userClient;
  }

  @Override
  @CircuitBreaker(name = "taskServiceCircuitBreaker", fallbackMethod = "findByUserIdFallback")
  public APIResponse<UserTasksDto> findAllByUserId(Long userId) {
    User user = userClient.findById(userId);
    List<Task> tasks = taskReadDatabaseService.findAllByUserId(userId);
    UserTasksDto data = new UserTasksDto(tasks, user);
    return new APIResponse<>(data, Collections.emptyList(), HttpStatus.OK, HttpStatus.OK.value());
  }

  public APIResponse<UserTasksDto> findByUserIdFallback(Long userId, Exception e) {
    List<Task> tasks = taskReadDatabaseService.findAllByUserId(userId);
    UserTasksDto data = new UserTasksDto(tasks, null);
    ToDoAppException exception = new ToDoAppException("FAILED_DEPENDENCY", e.getMessage());
    return new APIResponse<>(data, List.of(exception), PARTIAL_CONTENT, PARTIAL_CONTENT.value());
  }

  @Override
  @CircuitBreaker(name = "taskServiceCircuitBreaker")
  public TaskDto save(CreateTaskDto createTaskDto, Long userId) {
    User user = userClient.findById(userId);
    String title = createTaskDto.title();
    Optional<Task> optionalTask = taskReadDatabaseService.findByTitleAndUserId(title, userId);

    if (optionalTask.isPresent()) {
      throw new IllegalArgumentException(
          "The User with ID [%d] has already created a Task with title [%s]"
              .formatted(userId, title));
    }
    Task task = taskWriteDatabaseService.save(createTaskDto, userId);
    return new TaskDto(
        task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
  }

  @Override
  @CircuitBreaker(name = "taskServiceCircuitBreaker")
  public TaskDto update(Long id, UpdateTaskDto updateTaskDto) {
    Task task = taskReadDatabaseService.findByIdOrElseThrow(id);
    task = taskWriteDatabaseService.update(task, updateTaskDto);
    User user = userClient.findById(task.getUserId());
    return new TaskDto(
        task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
  }

  @Override
  @CircuitBreaker(name = "taskServiceCircuitBreaker")
  public TaskDto delete(Long id) {
    Task task = taskReadDatabaseService.findByIdOrElseThrow(id);
    taskWriteDatabaseService.delete(id);
    User user = userClient.findById(task.getUserId());
    return new TaskDto(
        task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
  }
}
