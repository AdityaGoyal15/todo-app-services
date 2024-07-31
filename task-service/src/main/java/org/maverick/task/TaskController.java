package org.maverick.task;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.TaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.maverick.task.dto.UserTasksDto;
import org.maverick.task.response.APIResponse;
import org.maverick.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public ResponseEntity<APIResponse<UserTasksDto>> findAllByUserId(@RequestParam Long userId) {
    APIResponse<UserTasksDto> response = taskService.findAllByUserId(userId);

    if (response.errors().isEmpty()) {
      return ResponseEntity.ok(response);
    }
    return new ResponseEntity<>(response, PARTIAL_CONTENT);
  }

  @PostMapping
  public ResponseEntity<APIResponse<TaskDto>> save(
      @RequestBody CreateTaskDto requestDto, @RequestParam Long userId) {
    APIResponse<TaskDto> response = taskService.save(requestDto, userId);

    if (response.errors().isEmpty()) {
      return new ResponseEntity<>(response, CREATED);
    }
    return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
  }

  @PutMapping("{id}")
  public ResponseEntity<APIResponse<TaskDto>> update(
      @PathVariable Long id, @RequestBody UpdateTaskDto requestDto) {
    APIResponse<TaskDto> response = taskService.update(id, requestDto);

    if (response.errors().isEmpty()) {
      return ResponseEntity.ok(response);
    }
    return new ResponseEntity<>(response, PARTIAL_CONTENT);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<APIResponse<TaskDto>> delete(@PathVariable Long id) {
    APIResponse<TaskDto> response = taskService.delete(id);

    if (response.errors().isEmpty()) {
      return ResponseEntity.ok(response);
    }
    return new ResponseEntity<>(response, PARTIAL_CONTENT);
  }
}
