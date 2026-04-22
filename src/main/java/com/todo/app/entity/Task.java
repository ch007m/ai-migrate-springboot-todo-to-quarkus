package com.todo.app.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task extends PanacheEntity {

  public String title;

  public String description;

  public LocalDate dueDate;

  public Task() {
  }

  public Task(String title, String description, LocalDate dueDate) {
    this.title = title;
    this.description = description;
    this.dueDate = dueDate;
  }
}
