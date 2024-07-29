package org.maverick.task.dto;

import org.maverick.task.external.User;

public record TaskDto(Long id, String title, String description, String status, User user) {}
