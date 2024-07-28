package org.maverick.user.dto;

import java.util.Objects;

public record UpdateUserDto(String firstName, String lastName, String email, String imageUrl) {
  public UpdateUserDto {
    if (Objects.isNull(email) || email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be null or empty");
    }
  }
}
