package org.maverick.task;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

  List<Task> findAllByUserId(Long userId);

  Optional<Task> findByTitleAndUserId(String title, Long userId);
}
