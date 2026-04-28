package com.todo.app.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tasks")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  private String description;

  // REMOVED: @DateTimeFormat(pattern = "yyyy-MM-dd")
  // Reason: @DateTimeFormat is a Spring-specific annotation for binding form/request params.
  // Replaced with @JsonFormat for JSON deserialization in REST endpoints.
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate dueDate;

  public Task() {
  }

  public Task(Long id, String title, String description, LocalDate dueDate) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.dueDate = dueDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }
}