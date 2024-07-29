package org.maverick.task.dto;

import java.util.Objects;

public record UpdateTaskDto(String title, String description, String status) {
  public UpdateTaskDto {
    if (Objects.isNull(title) || title.isBlank() || Objects.isNull(status) || status.isBlank()) {
      throw new IllegalArgumentException("Title and status cannot be null or blank");
    }
  }
}
