package org.example;

import org.example.controller.TaskController;
import org.example.model.InMemoryTaskRepository;
import org.example.model.TaskRepository;
import org.example.view.GameWindow;
import org.example.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {


        TaskRepository repository = new InMemoryTaskRepository();

        TaskController controller = new TaskController(repository);




        SwingUtilities.invokeLater(() ->{
           // MainFrame frame = new MainFrame(controller);
            //frame.setVisible(true);

            new GameWindow().setVisible(true);
        });
    }
}
