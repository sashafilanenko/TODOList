package org.example.model;

import java.util.List;

public interface TaskRepository {

    void save(Task task);

    void deleteById(int id);

    List<Task> findAll();

    List<Task> findAllHistory();

    void saveToHistory(Task task);
}
