package org.example.view;

import org.example.controller.TaskController;
import org.example.model.Task;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private TaskController controller;
    private DefaultListModel<Task> listModel;
    private JList<Task> taskList;

    private JTextField titleField;
    private JTextField descriptionField;

    public MainFrame(TaskController controller){
        this.controller =  controller;
        setTitle("TODO");
        setSize(600,400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();

        inputPanel.setLayout(new GridLayout(1, 3, 5,5));

        titleField = new JTextField();
        titleField.setBorder(BorderFactory.createTitledBorder("Заголовок"));

        descriptionField = new JTextField();
        descriptionField.setBorder(BorderFactory.createTitledBorder("Описание"));

        JButton addButton = new JButton("Добавить");

        addButton.addActionListener(e ->{
            String title = titleField.getText();
            String description = descriptionField.getText();

            if(!title.isEmpty()){
                controller.addTask(title,description);

                titleField.setText("");
                descriptionField.setText("");

                refreshTaskList();
            }
            else{
                JOptionPane.showMessageDialog(this, "заголовка нет, броу");
            }
        });



        inputPanel.add(titleField);
        inputPanel.add(descriptionField);
        inputPanel.add(addButton);


        add(inputPanel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        add(new JScrollPane(taskList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton deleteButton = new JButton("Удалить выбранное");

        deleteButton.addActionListener(e ->{
            Task selectedTask = taskList.getSelectedValue();

            if(selectedTask != null){
                controller.deleteTask(selectedTask.getId());
                refreshTaskList();
            }
            else {
                JOptionPane.showMessageDialog(this, "Ты ничего не выбрал, броу");
            }
        });

        bottomPanel.add(deleteButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTaskList();
    }

    private void refreshTaskList() {
        listModel.clear();

        for(Task task : controller.getAllTasks()){
            listModel.addElement(task);
        }
    }


}
