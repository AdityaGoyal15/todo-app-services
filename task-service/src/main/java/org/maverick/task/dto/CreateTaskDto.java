package org.maverick.task.dto;

import java.util.Objects;

public record CreateTaskDto(String title, String description) {
  public CreateTaskDto {
    if (Objects.isNull(title) || title.isBlank()) {
      throw new IllegalArgumentException("Title cannot be null or blank");
    }
  }
}
