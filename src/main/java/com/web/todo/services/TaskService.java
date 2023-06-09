package com.web.todo.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.todo.models.Task;
import com.web.todo.models.User;
import com.web.todo.repositories.TaskRepository;

@Service
public class TaskService {
  @Autowired
  private UserService userService;

  @Autowired
  private TaskRepository taskRepository;

  public Task findById(Long id) {
    Optional<Task> task = this.taskRepository.findById(id);
    return task.orElseThrow(() -> new RuntimeException(
      "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()
    ));
  }

  // Busca todas as tasks de um usuário
  public List<Task> findAllByUserId(Long userId) {
    // o 'findByUser_Id' é o método criado no 'task repository'
    List<Task> tasks = this.taskRepository.findByUser_Id(userId);
    return tasks;
  }

  @Transactional
  public Task create(Task obj) {
    User user = this.userService.findById(obj.getUser().getId());
    obj.setId(null);
    obj.setUser(user);
    obj = this.taskRepository.save(obj);
    return obj;
  }

  @Transactional
  public Task update(Task obj) {
    Task newObj = findById(obj.getId());
    newObj.setDescription(obj.getDescription());
    return this.taskRepository.save(newObj);
  }

  public void delete(Long id) {
    findById(id);
    try {
      this.taskRepository.deleteById(id);
    } catch(Exception e) {
      throw new RuntimeException("Não é possível excluir pois há entidades relacionadas!");
    }
  }
}
