package org.maverick.user.dto;

import java.util.Objects;

public record CreateUserDto(String firstName, String lastName, String email) {
  public CreateUserDto {
    if (Objects.isNull(email) || email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be null or empty");
    }
  }
}
