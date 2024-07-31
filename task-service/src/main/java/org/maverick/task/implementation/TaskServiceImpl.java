package org.maverick.task.implementation;

import static java.util.Collections.emptyList;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.exception.ConstraintViolationException;
import org.maverick.task.Task;
import org.maverick.task.client.UserClient;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.TaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.maverick.task.dto.UserTasksDto;
import org.maverick.task.exception.EntityNotFoundException;
import org.maverick.task.exception.ToDoAppException;
import org.maverick.task.external.User;
import org.maverick.task.response.APIResponse;
import org.maverick.task.service.TaskService;
import org.maverick.task.service.database.TaskReadDatabaseService;
import org.maverick.task.service.database.TaskWriteDatabaseService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

  private static final String SERVER_ERROR = "SERVER_ERROR";
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
    return new APIResponse<>(data, emptyList(), true);
  }

  public APIResponse<UserTasksDto> findByUserIdFallback(Long userId, Exception e) {
    boolean success = true;
    List<Task> tasks = new ArrayList<>();

    if (e instanceof FeignException) {
      success = false;
    } else {
      tasks = taskReadDatabaseService.findAllByUserId(userId);
    }
    UserTasksDto data = new UserTasksDto(tasks, null);
    ToDoAppException exception = new ToDoAppException(SERVER_ERROR, e.getMessage());
    return new APIResponse<>(data, List.of(exception), success);
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
    return new APIResponse<>(data, emptyList(), true);
  }

  public APIResponse<TaskDto> saveFallback(Exception e) {
    ToDoAppException exception = new ToDoAppException(SERVER_ERROR, e.getMessage());
    return new APIResponse<>(null, List.of(exception), false);
  }

  @Override
  @CircuitBreaker(name = "taskServiceCircuitBreaker", fallbackMethod = "updateFallback")
  public APIResponse<TaskDto> update(Long id, UpdateTaskDto updateTaskDto) {
    Task task = taskReadDatabaseService.findByIdOrElseThrow(id);
    task = taskWriteDatabaseService.update(task, updateTaskDto);
    User user = userClient.findById(task.getUserId());
    TaskDto data =
        new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
    return new APIResponse<>(data, emptyList(), true);
  }

  public APIResponse<TaskDto> updateFallback(Long id, UpdateTaskDto updateTaskDto, Exception e) {
    boolean success = true;
    TaskDto data =
        new TaskDto(
            id, updateTaskDto.title(), updateTaskDto.description(), updateTaskDto.status(), null);

    if (e instanceof EntityNotFoundException || e instanceof ConstraintViolationException) {
      success = false;
    }
    ToDoAppException exception = new ToDoAppException(SERVER_ERROR, e.getMessage());
    return new APIResponse<>(data, List.of(exception), success);
  }

  @Override
  @CircuitBreaker(name = "taskServiceCircuitBreaker", fallbackMethod = "deleteFallback")
  public APIResponse<TaskDto> delete(Long id) {
    Task task = taskReadDatabaseService.findByIdOrElseThrow(id);
    taskWriteDatabaseService.delete(id);
    User user = userClient.findById(task.getUserId());
    TaskDto data =
        new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
    return new APIResponse<>(data, emptyList(), true);
  }

  public APIResponse<TaskDto> deleteFallback(Long id, Exception e) {
    Task task;
    TaskDto data = null;
    boolean success = true;
    ToDoAppException exception = new ToDoAppException(SERVER_ERROR, e.getMessage());

    if (e instanceof EntityNotFoundException) {
      success = false;
    } else {
      task = taskReadDatabaseService.findByIdOrElseThrow(id);
      data =
          new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), null);
    }
    return new APIResponse<>(data, List.of(exception), success);
  }
}
