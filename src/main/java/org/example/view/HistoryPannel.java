package org.example.view;

import org.example.controller.TaskController;
import org.example.model.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryPannel extends JPanel {

    private TaskController controller;
    private JPanel historyListPanel;

    private static final Map<String, Color> CATEGORY_COLORS = new HashMap<>();
    static {
        CATEGORY_COLORS.put("Красная зона", new Color(255, 220, 220));
        CATEGORY_COLORS.put("Зеленая зона", new Color(220, 255, 220));
        CATEGORY_COLORS.put("Синяя зона", new Color(220, 220, 255));
        CATEGORY_COLORS.put("Желтая зона", new Color(255, 255, 220));
    }

    public HistoryPannel(TaskController controller) {
        this.controller = controller;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initializeComponents();
        loadHistory();
    }

    private void initializeComponents() {
        // Заголовок
        JLabel title = new JLabel("История удаленных задач", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Панель для списка истории
        historyListPanel = new JPanel();
        historyListPanel.setLayout(new BoxLayout(historyListPanel, BoxLayout.Y_AXIS));
        historyListPanel.setBackground(Color.WHITE);
        historyListPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(historyListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Кнопка обновления
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton refreshButton = new JButton("Обновить");
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        refreshButton.setPreferredSize(new Dimension(120, 40));
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadHistory());

        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadHistory() {
        historyListPanel.removeAll();

        List<Task> historyTasks = controller.getAllHistoryTasks();

        if (historyTasks.isEmpty()) {
            JLabel emptyLabel = new JLabel("История пуста", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(new EmptyBorder(50, 0, 0, 0));
            historyListPanel.add(emptyLabel);
        } else {
            for (Task task : historyTasks) {
                historyListPanel.add(createHistoryItem(task));
                historyListPanel.add(Box.createVerticalStrut(5));
            }
        }

        historyListPanel.revalidate();
        historyListPanel.repaint();

        System.out.println("История загружена: " + historyTasks.size() + " задач");
    }

    private JPanel createHistoryItem(Task task) {
        Color categoryColor = CATEGORY_COLORS.getOrDefault(task.getCategory(), Color.LIGHT_GRAY);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(categoryColor);
        panel.setBorder(new LineBorder(categoryColor.darker(), 2));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(categoryColor);
        leftPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel idLabel = new JLabel("ID: " + task.getId());
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel categoryLabel = new JLabel(task.getCategory());
        categoryLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        categoryLabel.setForeground(categoryColor.darker().darker());

        leftPanel.add(idLabel);
        leftPanel.add(categoryLabel);
        panel.add(leftPanel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel(task.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setBorder(new EmptyBorder(5, 15, 5, 15));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(titleLabel, BorderLayout.CENTER);

        if (task.getCompleteAt() != null) {
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setBackground(categoryColor);
            rightPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

            JLabel dateLabel = new JLabel("Выполнено:");
            dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));

            JLabel timeLabel = new JLabel(formatDateTime(task.getCompleteAt()));
            timeLabel.setFont(new Font("SansSerif", Font.BOLD, 11));

            rightPanel.add(dateLabel);
            rightPanel.add(timeLabel);
            panel.add(rightPanel, BorderLayout.EAST);
        }

        return panel;
    }

    private String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return "";

        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTime.format(formatter);
    }

    public void refreshHistory() {
        loadHistory();
    }
}