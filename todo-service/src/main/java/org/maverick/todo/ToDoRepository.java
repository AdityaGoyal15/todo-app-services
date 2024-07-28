package org.maverick.todo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {

  List<ToDo> findAllByUserId(Long userId);

  Optional<ToDo> findByTitleAndUserId(String title, Long userId);
}
