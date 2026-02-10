package org.example.controller;

import org.example.model.Task;
import org.example.model.TaskRepository;

import java.util.List;

public class TaskController {

    private final TaskRepository repository;


    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks(){
        return repository.findAll();
    }

    public void addTask(String title, String description){
        Task newTask = new Task(title, description);
        repository.save(newTask);
    }

    public void deleteTask(int id){
        repository.deleteById(id);
    }
}
