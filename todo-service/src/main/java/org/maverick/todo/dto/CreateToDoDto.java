package org.maverick.todo.dto;

import java.util.Objects;

public record CreateToDoDto(String title, String description) {
  public CreateToDoDto {
    if (Objects.isNull(title) || title.isBlank()) {
      throw new IllegalArgumentException("Title cannot be null or blank");
    }
  }
}
