package org.example;

import org.example.Game.GameController;
import org.example.controller.TaskController;
import org.example.model.InMemoryTaskRepository;
import org.example.model.TaskRepository;
import org.example.view.GameWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        TaskRepository repository = new InMemoryTaskRepository();

        GameController gameController = new GameController();

        TaskController controller = new TaskController(repository, gameController);

        SwingUtilities.invokeLater(() ->{
            new GameWindow(controller, gameController).setVisible(true);
        });
    }
}