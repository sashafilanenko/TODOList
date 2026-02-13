package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskRepository implements TaskRepository{

    private List<Task> tasks = new ArrayList<>();
    private  List<Task> historyTasks = new ArrayList<>();

    private int currentId = 1;

    @Override
    public void save(Task task){
        if(task.getId() == 0){
            task.setId(currentId);
            currentId++;
            tasks.add(task);
        }
    }

    @Override
    public void saveToHistory(Task task){
        historyTasks.add(task);
        task.setCompleteAt(LocalDateTime.now());
    }

    @Override
    public void deleteById(int id) {
        Task taskToDelete = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElse(null);

        if (taskToDelete != null) {
            saveToHistory(taskToDelete);

            tasks.remove(taskToDelete);
        }
    }

    @Override
    public List<Task> findAll(){
        return new ArrayList<>(tasks);
    }

    @Override
    public List<Task> findAllHistory(){
        return new ArrayList<>(historyTasks);
    }
}
