package org.maverick.task.implementation;

import java.util.List;
import java.util.Optional;
import org.maverick.task.Task;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.TaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.maverick.task.dto.UserTasksDto;
import org.maverick.task.external.User;
import org.maverick.task.service.TaskService;
import org.maverick.task.service.database.TaskReadDatabaseService;
import org.maverick.task.service.database.TaskWriteDatabaseService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskReadDatabaseService taskReadDatabaseService;
  private final TaskWriteDatabaseService taskWriteDatabaseService;

  public TaskServiceImpl(
      TaskReadDatabaseService taskReadDatabaseService,
      TaskWriteDatabaseService taskWriteDatabaseService) {
    this.taskReadDatabaseService = taskReadDatabaseService;
    this.taskWriteDatabaseService = taskWriteDatabaseService;
  }

  @Override
  public UserTasksDto findAllByUserId(Long userId) {
    User user = getUser(userId);
    List<Task> tasks = taskReadDatabaseService.findAllByUserId(userId);
    return new UserTasksDto(tasks, user);
  }

  @Override
  public TaskDto save(CreateTaskDto createTaskDto, Long userId) {
    User user = getUser(userId);
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
  public TaskDto update(Long id, UpdateTaskDto updateTaskDto) {
    Task task = taskReadDatabaseService.findByIdOrElseThrow(id);
    task = taskWriteDatabaseService.update(task, updateTaskDto);
    User user = getUser(task.getUserId());
    return new TaskDto(
        task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
  }

  @Override
  public TaskDto delete(Long id) {
    Task task = taskReadDatabaseService.findByIdOrElseThrow(id);
    taskWriteDatabaseService.delete(id);
    User user = getUser(task.getUserId());
    return new TaskDto(
        task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), user);
  }

  private static User getUser(Long userId) {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForObject("http://localhost:8081/users/" + userId, User.class);
  }
}
