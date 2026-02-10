package org.example.view;

import org.example.controller.TaskController;

import javax.swing.*;

public class MainFrame extends JFrame {

    private TaskController controller;

    public MainFrame(TaskController controller){
        this.controller =  controller;
        setTitle("TODO");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }



}
