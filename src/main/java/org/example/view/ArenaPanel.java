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
    private JLabel tomatoLabel;
    private JLabel dailyStreakLabel;
    private Timer timer;
    private int remainingSeconds = 25 * 60;
    private boolean isRunning = false;

    private JPanel tasksPanel;
    private JPanel leftPanel;

    private GameController gameController;

    private int pomodoroCount = 0;
    private boolean isBreakPhase = false;
    private int streakMultiplierLevel = 0;

    private static final Map<String, Color> CATEGORY_COLORS = new HashMap<>();
    static {
        CATEGORY_COLORS.put("Красная зона", new Color(255, 220, 220));
        CATEGORY_COLORS.put("Зеленая зона", new Color(220, 255, 220));
        CATEGORY_COLORS.put("Синяя зона",   new Color(220, 220, 255));
        CATEGORY_COLORS.put("Желтая зона",  new Color(255, 255, 220));
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
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        categoryLabel = new JLabel("");
        categoryLabel.setFont(new Font("Serif", Font.BOLD, 28));
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(categoryLabel, gbc);

        gbc.gridy = 1;
        dailyStreakLabel = new JLabel(gameController.getDailyStreakLabel());
        dailyStreakLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        dailyStreakLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dailyStreakLabel.setToolTipText("Ежедневный стрик — выполняй хотя бы одну задачу в день чтобы сохранить огонёк!");
        panel.add(dailyStreakLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(16, 10, 16, 10);
        timerLabel = new JLabel("25:00");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 64));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(timerLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 8, 10);
        tomatoLabel = new JLabel("");
        tomatoLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        tomatoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tomatoLabel.setToolTipText("Каждые 4 помодоро — длинный перерыв");
        panel.add(tomatoLabel, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(8, 10, 8, 10);
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

        gbc.gridy = 5;
        gbc.insets = new Insets(4, 10, 4, 10);
        JLabel multiplierHint = new JLabel(buildMultiplierHint());
        multiplierHint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        multiplierHint.setForeground(Color.GRAY);
        multiplierHint.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(multiplierHint, gbc);

        timer = new Timer(1000, e -> updateTimer());

        return panel;
    }

    private String buildMultiplierHint() {
        double dm = gameController.getDailyStreakMultiplier();
        int ds = gameController.getDailyStreak();
        if (ds == 0) return "Выполни задачу сегодня чтобы начать стрик!";
        if (dm == 1.0) return "Стрик " + ds + " дн. — ещё " + (3 - ds) + " дня до бонуса x1.3";
        return String.format("Стрик %d дн. — бонус к XP ?%.1f!", ds, dm);
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

            for (Component comp : leftPanel.getComponents()) {
                if (comp instanceof JLabel header) {
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
        deleteCheckBox.setToolTipText("Отметить как выполненную");
        deleteCheckBox.addActionListener(e -> {
            if (deleteCheckBox.isSelected()) {
                controller.deleteTask(task.getId());
                System.out.println("========== Удаление задачи ID:" + task.getId() + " ==========");
                loadTasks();
                refreshStreakUI();
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
        }
    }

    private void stopTimer() {
        if (isRunning) {
            isRunning = false;
            timer.stop();

            if (!isBreakPhase) {
                streakMultiplierLevel = 0;
                gameController.resetCharacterStreak();
                gameController.addXPToCharacter(-20);
                gameWindow.updateCharacterUI();
                showPenaltyMessage();
            }
        }
    }

    private void resetTimer() {
        if (isRunning) {
            isRunning = false;
            timer.stop();
        }
        isBreakPhase = false;
        remainingSeconds = 25 * 60;
        updateTimerLabel();
        updatePhaseUI();
    }

    private void updateTimer() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
            updateTimerLabel();
        } else {
            timer.stop();
            isRunning = false;

            if (!isBreakPhase) {
                onPomodoroComplete();
            } else {
                onBreakComplete();
            }
        }
    }

    private void onPomodoroComplete() {
        pomodoroCount++;
        streakMultiplierLevel++;

        int baseXP = getCategoryBaseXP();
        double pomodoroMultiplier = getPomodoroMultiplier();

        int earned = gameController.calculateAndAddXP(baseXP, pomodoroMultiplier);

        gameController.incrementCharacterStreak();
        gameWindow.updateCharacterUI();

        updateTomatoDisplay();
        refreshStreakUI();
        showRewardPopup(earned, pomodoroMultiplier);

        isBreakPhase = true;
        remainingSeconds = (pomodoroCount % 4 == 0) ? 15 * 60 : 5 * 60;
        updatePhaseUI();
        timer.start();
        isRunning = true;
    }

    private void onBreakComplete() {
        isBreakPhase = false;
        remainingSeconds = 25 * 60;
        updatePhaseUI();
        showBreakEndPopup();
    }

    private int getCategoryBaseXP() {
        return switch (currentCategory) {
            case "Красная зона" -> 60;
            case "Зеленая зона" -> 45;
            case "Синяя зона"   -> 30;
            case "Желтая зона"  -> 20;
            default -> 25;
        };
    }

    private double getPomodoroMultiplier() {
        return switch (streakMultiplierLevel) {
            case 1  -> 1.0;
            case 2  -> 1.2;
            case 3  -> 1.5;
            default -> 2.0;
        };
    }

    public void refreshStreakUI() {
        dailyStreakLabel.setText(gameController.getDailyStreakLabel());
    }

    private void updatePhaseUI() {
        if (isBreakPhase) {
            timerLabel.setForeground(new Color(0, 150, 0));
            categoryLabel.setText("? Перерыв");
        } else {
            timerLabel.setForeground(Color.BLACK);
            if (currentCategory != null) {
                categoryLabel.setText(currentCategory);
            }
        }
    }

    private void updateTomatoDisplay() {
        int tomatoesInCycle = pomodoroCount % 4;
        if (tomatoesInCycle == 0) tomatoesInCycle = 4;
        tomatoLabel.setText("?".repeat(tomatoesInCycle));
    }

    private void showRewardPopup(int earned, double pomodoroMultiplier) {
        double dailyMultiplier = gameController.getDailyStreakMultiplier();
        int dailyStreak = gameController.getDailyStreak();

        String pomodoroLine = pomodoroMultiplier > 1.0
                ? String.format("Серия помодоро: ?%.1f", pomodoroMultiplier)
                : "Серия помодоро: без бонуса";

        String dailyLine = dailyMultiplier > 1.0
                ? String.format("Дневной стрик %d дн.: ?%.1f ?", dailyStreak, dailyMultiplier)
                : "Дневной стрик: без бонуса пока (нужно 3 дня)";

        String breakInfo = (pomodoroCount % 4 == 0)
                ? "\n\n? Заработан длинный перерыв 15 минут!"
                : "\nНачинается короткий перерыв 5 минут.";

        JOptionPane.showMessageDialog(
                this,
                "Помодоро завершён! ?\n"
                        + "Получено XP: +" + earned + "\n\n"
                        + pomodoroLine + "\n"
                        + dailyLine
                        + breakInfo,
                "Награда",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showBreakEndPopup() {
        JOptionPane.showMessageDialog(
                this,
                "Перерыв окончен! Готов к следующему помодоро?\nНажми Старт когда будешь готов. ?",
                "Перерыв завершён",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showPenaltyMessage() {
        JOptionPane.showMessageDialog(
                this,
                "Сессия прервана! -20 XP\nСерия помодоро сброшена. ?",
                "Штраф",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void updateTimerLabel() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }
}