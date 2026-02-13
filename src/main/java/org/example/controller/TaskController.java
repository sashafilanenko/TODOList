package org.example.controller;

import org.example.Game.GameController;
import org.example.model.Task;
import org.example.model.TaskRepository;

import java.util.List;

public class TaskController {

    private final TaskRepository repository;
    private final GameController gameController;

    public TaskController(TaskRepository repository, GameController gameController) {
        this.repository = repository;
        this.gameController = gameController;
    }

    public List<Task> getAllTasks(){
        return repository.findAll();
    }

    public void addTask(String title, String category){
        Task newTask = new Task(title, category);
        repository.save(newTask);
    }

    public void saveToHistory(Task task){
        repository.saveToHistory(task);
    }

    public List<Task> getAllHistoryTasks(){
        return repository.findAllHistory();
    }

    public void deleteTask(int id){
        Task task = findTaskById(id);


        if (task != null) {
            String cat = task.getCategory();

            switch (cat){
                case "Красная зона" -> gameController.addXPToCharacter(40);
                case "Зеленая зона" -> gameController.addXPToCharacter(30);
                case "Синяя зона" -> gameController.addXPToCharacter(20);
                case "Желтая зона" -> gameController.addXPToCharacter(10);
            }

            repository.deleteById(id);

        }
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
}