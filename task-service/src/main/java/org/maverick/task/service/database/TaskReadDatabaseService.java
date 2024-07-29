package org.maverick.task.service.database;

import java.util.List;
import java.util.Optional;
import org.maverick.task.Task;

public interface TaskReadDatabaseService {

  List<Task> findAllByUserId(Long userId);

  Optional<Task> findByTitleAndUserId(String title, Long userId);
}
