package org.maverick.todo.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import org.maverick.todo.ToDo;
import org.maverick.todo.ToDoRepository;
import org.maverick.todo.ToDoService;
import org.maverick.todo.dto.CreateToDoDto;
import org.maverick.todo.dto.UpdateToDoDto;
import org.maverick.todo.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ToDoServiceImpl implements ToDoService {

  private final ToDoRepository toDoRepository;

  public ToDoServiceImpl(ToDoRepository toDoRepository) {
    this.toDoRepository = toDoRepository;
  }

  @Override
  public List<ToDo> findAllByUserId(Long userId) {
    return toDoRepository.findAllByUserId(userId);
  }

  @Override
  @Transactional
  public ToDo save(CreateToDoDto requestDto, Long userId) {
    String title = requestDto.title();
    ToDo todo = findByUserIdAndTitleOrElseNull(userId, title);

    if (todo != null) {
      throw new IllegalArgumentException(
          "The User with ID [%d] has already created a ToDo with title [%s]"
              .formatted(userId, title));
    }

    todo = new ToDo();
    todo.setTitle(title);
    todo.setDescription(requestDto.description());
    todo.setStatus("TODO");
    todo.setUserId(userId);
    return toDoRepository.save(todo);
  }

  private ToDo findByUserIdAndTitleOrElseNull(Long userId, String title) {
    return toDoRepository.findByTitleAndUserId(title, userId).orElse(null);
  }

  @Override
  @Transactional
  public ToDo update(Long id, UpdateToDoDto requestDto) {
    ToDo todo = findByIdOrElseThrow(id);
    todo.setTitle(requestDto.title());
    todo.setDescription(requestDto.description());
    todo.setStatus(requestDto.status());
    return toDoRepository.save(todo);
  }

  private ToDo findByIdOrElseThrow(Long id) {
    return toDoRepository
        .findById(id)
        .orElseThrow(
            () -> new EntityNotFoundException("ToDo with ID [%d] not found".formatted(id)));
  }

  @Override
  @Transactional
  public ToDo delete(Long id) {
    ToDo todo = findByIdOrElseThrow(id);
    toDoRepository.deleteById(id);
    return todo;
  }
}
