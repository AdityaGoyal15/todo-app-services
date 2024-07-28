package org.maverick.todo;

import java.util.List;
import org.maverick.todo.dto.CreateToDoDto;
import org.maverick.todo.dto.UpdateToDoDto;

public interface ToDoService {

  List<ToDo> findAllByUserId(Long userId);

  ToDo save(CreateToDoDto requestDto, Long userId);

  ToDo update(Long id, UpdateToDoDto requestDto);

  ToDo delete(Long id);
}
