package org.maverick.task;

import java.util.List;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.UpdateTaskDto;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<List<Task>> findAllByUserId(@RequestParam Long userId) {
    return ResponseEntity.ok(taskService.findAllByUserId(userId));
  }

  @PostMapping
  public ResponseEntity<Task> save(
      @RequestBody CreateTaskDto requestDto, @RequestParam Long userId) {
    return new ResponseEntity<>(taskService.save(requestDto, userId), HttpStatus.CREATED);
  }

  @PutMapping("{id}")
  public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody UpdateTaskDto requestDto) {
    return ResponseEntity.ok(taskService.update(id, requestDto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Task> delete(@PathVariable Long id) {
    return ResponseEntity.ok(taskService.delete(id));
  }
}
