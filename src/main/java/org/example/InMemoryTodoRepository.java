package org.example;

import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTodoRepository implements TodoRepository {
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public TODO save(TODO todo) {
        Long id = seq.incrementAndGet();
        return new TODO(id, todo.getText());
    }
}