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

    public void addTask(String title, String category){
        Task newTask = new Task(title, category);
        repository.save(newTask);
    }

    public void deleteTask(int id){
        repository.deleteById(id);
    }


    public Task findTaskById(int id) {
        for (Task task : getAllTasks()) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public void updateTask(int id, String newTitle) {
        Task task = findTaskById(id);
        if (task != null) {
            task.setTitle(newTitle);
        }
    }

    public void updateTaskCategory(int id, String newCategory) {
        Task task = findTaskById(id);
        if (task != null) {
            task.setCategory(newCategory);
        }
    }

    public void updateTask(int id, String newTitle, String newCategory) {
        Task task = findTaskById(id);
        if (task != null) {
            task.setTitle(newTitle);
            task.setCategory(newCategory);
        }
    }

}
