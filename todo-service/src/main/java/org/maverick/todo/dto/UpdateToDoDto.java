package org.maverick.todo.dto;

import java.util.Objects;

public record UpdateToDoDto(String title, String description, String status) {
  public UpdateToDoDto {
    if (Objects.isNull(title) || title.isBlank() || Objects.isNull(status) || status.isBlank()) {
      throw new IllegalArgumentException("Title and status cannot be null or blank");
    }
  }
}
