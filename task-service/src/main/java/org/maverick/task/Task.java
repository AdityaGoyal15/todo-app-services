package org.maverick.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.maverick.task.dto.CreateTaskDto;
import org.maverick.task.dto.UpdateTaskDto;

@Entity
@Table(
    name = "tasks",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "userId"})})
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  private String description;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false)
  private Long userId;

  public Task() {}

  public Task(CreateTaskDto dto, Long userId) {
    this.title = dto.title();
    this.description = dto.description();
    this.status = "To Do";
    this.userId = userId;
  }

  public void update(UpdateTaskDto dto) {
    this.title = dto.title();
    this.description = dto.description();
    this.status = dto.status();
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getStatus() {
    return status;
  }

  public Long getUserId() {
    return userId;
  }
}
