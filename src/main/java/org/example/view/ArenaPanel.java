package org.example.view;

import org.example.Game.GameController;
import org.example.controller.TaskController;
import org.example.model.Task;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaPanel extends JPanel {

    private GameWindow gameWindow;
    private TaskController controller;
    private String currentCategory;
    private Color currentCategoryColor;

    private JLabel timerLabel;
    private JLabel categoryLabel;
    private Timer timer;
    private int remainingSeconds = 25 * 60;
    private boolean isRunning = false;

    private JPanel tasksPanel;
    private JPanel leftPanel;

    GameController gameController;

    private int pomodoroCount = 0;
    private boolean isBreakPhase = false;
    private int streakMultiplierLevel = 0;

    private static final Map<String, Color> CATEGORY_COLORS = new HashMap<>();
    static {
        CATEGORY_COLORS.put("Красная зона", new Color(255, 220, 220));
        CATEGORY_COLORS.put("Зеленая зона", new Color(220, 255, 220));
        CATEGORY_COLORS.put("Синяя зона", new Color(220, 220, 255));
        CATEGORY_COLORS.put("Желтая зона", new Color(255, 255, 220));
    }


    public ArenaPanel(TaskController controller, GameWindow gameWindow, GameController gameController) {
        this.controller = controller;
        this.gameWindow = gameWindow;
        this.gameController = gameController;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initializeComponents();
    }


    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        leftPanel = createTasksPanel();
        leftPanel.setPreferredSize(new Dimension(300, 0));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = createTimerPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createTasksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        JLabel header = new JLabel("Задачи", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 18));
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        panel.add(header, BorderLayout.NORTH);

        tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        tasksPanel.setBackground(Color.WHITE);
        tasksPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(tasksPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTimerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        categoryLabel = new JLabel("");
        categoryLabel.setFont(new Font("Serif", Font.BOLD, 28));
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(categoryLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 10, 20, 10);
        timerLabel = new JLabel("25:00");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 64));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(timerLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setBackground(Color.WHITE);

        JButton startButton = new JButton("Старт");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        startButton.setPreferredSize(new Dimension(100, 45));
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> startTimer());

        JButton stopButton = new JButton("Стоп");
        stopButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        stopButton.setPreferredSize(new Dimension(100, 45));
        stopButton.setFocusPainted(false);
        stopButton.addActionListener(e -> stopTimer());

        JButton resetButton = new JButton("Сброс");
        resetButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        resetButton.setPreferredSize(new Dimension(100, 45));
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> resetTimer());

        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(resetButton);

        panel.add(buttonsPanel, gbc);

        timer = new Timer(1000, e -> updateTimer());

        return panel;
    }

    public void loadCategory(String category) {
        this.currentCategory = category;
        this.currentCategoryColor = CATEGORY_COLORS.getOrDefault(category, Color.WHITE);

        categoryLabel.setText(category);

        updateLeftPanelColor();

        loadTasks();
    }

    private void updateLeftPanelColor() {
        if (leftPanel != null && currentCategoryColor != null) {
            leftPanel.setBackground(currentCategoryColor);

            Component[] components = leftPanel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel header = (JLabel) comp;
                    header.setBackground(currentCategoryColor.darker());
                    header.setOpaque(true);
                }
            }

            tasksPanel.setBackground(currentCategoryColor);
        }
    }

    private void loadTasks() {
        tasksPanel.removeAll();

        List<Task> allTasks = controller.getAllTasks();
        for (Task task : allTasks) {
            if (task.getCategory().equals(currentCategory)) {
                tasksPanel.add(createTaskRow(task));
            }
        }

        tasksPanel.revalidate();
        tasksPanel.repaint();
    }

    private JPanel createTaskRow(Task task) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(currentCategoryColor);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel taskLabel = new JLabel(task.getTitle());
        taskLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        taskLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        panel.add(taskLabel, BorderLayout.CENTER);

        JCheckBox deleteCheckBox = new JCheckBox();
        deleteCheckBox.setBackground(currentCategoryColor);
        deleteCheckBox.setToolTipText("Удалить задачу");
        deleteCheckBox.addActionListener(e -> {
            if (deleteCheckBox.isSelected()) {
                controller.deleteTask(task.getId());
                System.out.println("========== Удаление задачи ID:" + task.getId() + " ==========");
                loadTasks();
                gameWindow.updateCharacterUI();
            }
        });

        panel.add(deleteCheckBox, BorderLayout.WEST);

        return panel;
    }

    private void startTimer() {
        if (!isRunning) {
            isRunning = true;
            timer.start();
            System.out.println("Таймер запущен");
        }
    }

    private void stopTimer() {
        if (isRunning) {
            isRunning = false;
            timer.stop();
            System.out.println("Таймер остановлен");
            gameController.addXPToCharacter(-20);
            gameWindow.updateCharacterUI();
        }
    }

    private void resetTimer() {
        stopTimer();
        remainingSeconds = 25 * 60;
        updateTimerLabel();
        System.out.println("Таймер сброшен");
    }

    private void updateTimer() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
            updateTimerLabel();
        } else {
            stopTimer();
            JOptionPane.showMessageDialog(this, "Время вышло!", "Таймер", JOptionPane.INFORMATION_MESSAGE);
            resetTimer();
        }
    }

    private void updateTimerLabel() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
}