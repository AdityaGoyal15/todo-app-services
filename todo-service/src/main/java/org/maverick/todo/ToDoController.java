package org.maverick.todo;

import java.util.List;
import org.maverick.todo.dto.CreateToDoDto;
import org.maverick.todo.dto.UpdateToDoDto;
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
@RequestMapping("todos")
public class ToDoController {

  private final ToDoService toDoService;

  public ToDoController(ToDoService toDoService) {
    this.toDoService = toDoService;
  }

  @GetMapping
  public ResponseEntity<List<ToDo>> findAllByUserId(@RequestParam Long userId) {
    return ResponseEntity.ok(toDoService.findAllByUserId(userId));
  }

  @PostMapping
  public ResponseEntity<ToDo> save(
      @RequestBody CreateToDoDto requestDto, @RequestParam Long userId) {
    return new ResponseEntity<>(toDoService.save(requestDto, userId), HttpStatus.CREATED);
  }

  @PutMapping("{id}")
  public ResponseEntity<ToDo> update(
      @PathVariable Long id, @RequestBody UpdateToDoDto requestDto) {
    return ResponseEntity.ok(toDoService.update(id, requestDto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<ToDo> delete(@PathVariable Long id) {
    return ResponseEntity.ok(toDoService.delete(id));
  }
}
