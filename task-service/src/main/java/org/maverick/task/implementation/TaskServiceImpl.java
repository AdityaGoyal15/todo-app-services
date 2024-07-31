package org.maverick.task.implementation;

import static java.util.Collections.emptyList;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

  private static final String CIRCUIT_OPEN = "CIRCUIT_OPEN";
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
    return new APIResponse<>(data, emptyList());
  }

  public APIResponse<UserTasksDto> findByUserIdFallback(Long userId, Exception e) {
    List<Task> tasks = taskReadDatabaseService.findAllByUserId(userId);
    UserTasksDto data = new UserTasksDto(tasks, null);
    return new APIResponse<>(data, emptyList());
  }

  @Override
  @CircuitBreaker(name = "taskServiceCircuitBreaker", fallbackMethod = "saveFallback")
  public APIResponse<TaskDto> save(CreateTaskDto createTaskDto, Long userId) {
    User user = userClient.findById(userId);
    String title = createTaskDto.title();
    Optional<Task> optionalTask = taskReadDatabaseService.findByTitleAndUserId(title, userId);

    if (optionalTask.isPresent()) {
      throw new IllegalArgumentException(
          "The User with ID [%d] has already created a Task with title [%s]"
              .formatted(userId, title));
    }
    Task task = taskWriteDatabaseService.save(createTaskDto, userId);
    TaskDto data =
        new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
    return new APIResponse<>(data, emptyList());
  }

  public APIResponse<TaskDto> saveFallback(Exception e) {
    ToDoAppException exception = new ToDoAppException(CIRCUIT_OPEN, e.getMessage());
    return new APIResponse<>(null, List.of(exception));
  }

  @Override
  @CircuitBreaker(name = "taskServiceCircuitBreaker", fallbackMethod = "updateFallback")
  public APIResponse<TaskDto> update(Long id, UpdateTaskDto updateTaskDto) {
    Task task = taskReadDatabaseService.findByIdOrElseThrow(id);
    task = taskWriteDatabaseService.update(task, updateTaskDto);
    User user = userClient.findById(task.getUserId());
    TaskDto data =
        new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
    return new APIResponse<>(data, emptyList());
  }

  public APIResponse<TaskDto> updateFallback(Long id, UpdateTaskDto updateTaskDto, Exception e) {
    TaskDto data =
        new TaskDto(
            id, updateTaskDto.title(), updateTaskDto.description(), updateTaskDto.status(), null);
    return new APIResponse<>(data, emptyList());
  }

  @Override
  @CircuitBreaker(name = "taskServiceCircuitBreaker", fallbackMethod = "deleteFallback")
  public APIResponse<TaskDto> delete(Long id) {
    Task task = taskReadDatabaseService.findByIdOrElseThrow(id);
    taskWriteDatabaseService.delete(id);
    User user = userClient.findById(task.getUserId());
    TaskDto data =
        new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
    return new APIResponse<>(data, emptyList());
  }

  public APIResponse<TaskDto> deleteFallback(Exception e) {
    return new APIResponse<>(null, emptyList());
  }
}
