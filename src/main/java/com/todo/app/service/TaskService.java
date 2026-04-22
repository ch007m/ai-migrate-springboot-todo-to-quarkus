package com.todo.app.service;

import com.todo.app.entity.Task;
import com.todo.app.repository.TaskRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TaskService {

  @Inject
  TaskRepository taskRepository;

  @Transactional
  public void addTask(Task task) {
    taskRepository.persist(task);
  }

  @Transactional
  public void deleteTaskById(Long id) {
    taskRepository.deleteById(id);
  }

  @Transactional
  public void updateTaskById(Long id, Task task) {
    Task existing = taskRepository.findById(id);
    if (existing != null) {
      existing.title = task.title;
      existing.description = task.description;
      existing.dueDate = task.dueDate;
    }
  }

  public List<Task> getAllTasks() {
    return taskRepository.listAll();
  }

  @Transactional
  public void deleteTask(Long taskId) {
    taskRepository.deleteById(taskId);
  }

  public List<Task> getTasksPage(int pageNo, int pageSize) {
    PanacheQuery<Task> query = taskRepository.findAll();
    query.page(Page.of(pageNo - 1, pageSize));
    return query.list();
  }

  public long countTasks() {
    return taskRepository.count();
  }
}
