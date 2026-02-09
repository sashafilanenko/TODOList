package org.example;

public class Main {
    public static void main(String[] args) {
        TodoRepository repo = new InMemoryTodoRepository();
        TodoService service = new TodoService(repo);

        TODO t = service.createTodo("Buy milk");
        System.out.println("Created todo: id=" + t.getId() + ", text=" + t.getText());
    }
}
