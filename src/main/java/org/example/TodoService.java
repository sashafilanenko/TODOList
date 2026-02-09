package org.example;

public class TodoService {
    private final TodoRepository repository;

    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public TODO createTodo(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text must not be empty");
        }
        TODO toSave = new TODO(null, text);
        return repository.save(toSave);
    }
}
