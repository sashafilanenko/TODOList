package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskRepository implements TaskRepository{

    private List<Task> tasks = new ArrayList<>();

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
    public void deleteById(int id){
        tasks.removeIf(t -> t.getId() == id);
    }

    @Override
    public List<Task> findAll(){
        return new ArrayList<>(tasks);
    }
}
