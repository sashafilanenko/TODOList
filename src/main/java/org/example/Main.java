package org.example;

import org.example.model.InMemoryTaskRepository;
import org.example.model.Task;
import org.example.model.TaskRepository;
import org.example.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {


        TaskRepository repository = new InMemoryTaskRepository();

        // 2. Создаем пару задач
        Task t1 = new Task("Убраться", "Помыть полы");
        Task t2 = new Task("Учеба", "Выучить SOLID");
        Task t3 = new Task("111", "Выучить xjnnj");

        // 3. Сохраняем их (тут им присвоятся ID)
        repository.save(t1);
        repository.save(t2);

        // 4. Проверяем, что они сохранились и получили ID
        System.out.println("Все задачи:");
        for (Task t : repository.findAll()) {
            System.out.println(t);
        }

        // 5. Пробуем удалить первую задачу
        repository.deleteById(1);
        repository.save(t3);

        System.out.println("После удаления ID=1:");
        for (Task t : repository.findAll()) {
            System.out.println(t);
        }

        SwingUtilities.invokeLater(() ->{
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
