package org.example.view;

import org.example.controller.TaskController;
import org.example.model.Task;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPanel extends JPanel {

    private GameWindow gameWindow;
    private static Component currentDraggingComponent = null;
    private static Point pressPoint = null;
    private TaskController controller;
    private int globalCounter = 1;
    private Map<String, KanbanColumn> categoryToColumn;
    private static final String RED_ZONE = "Красная зона";
    private static final String GREEN_ZONE = "Зеленая зона";
    private static final String BLUE_ZONE = "Синяя зона";
    private static final String YELLOW_ZONE = "Желтая зона";

    public MapPanel(TaskController controller, GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        this.controller = controller;
        this.categoryToColumn = new HashMap<>();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Карта битвы", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        centerGrid.setBackground(new Color(230, 230, 230));
        centerGrid.setBorder(new EmptyBorder(10, 10, 10, 10));

        KanbanColumn redColumn = new KanbanColumn(new Color(255, 220, 220), RED_ZONE);
        KanbanColumn greenColumn = new KanbanColumn(new Color(220, 255, 220), GREEN_ZONE);
        KanbanColumn blueColumn = new KanbanColumn(new Color(220, 220, 255), BLUE_ZONE);
        KanbanColumn yellowColumn = new KanbanColumn(new Color(255, 255, 220), YELLOW_ZONE);

        categoryToColumn.put(RED_ZONE, redColumn);
        categoryToColumn.put(GREEN_ZONE, greenColumn);
        categoryToColumn.put(BLUE_ZONE, blueColumn);
        categoryToColumn.put(YELLOW_ZONE, yellowColumn);

        centerGrid.add(redColumn);
        centerGrid.add(greenColumn);
        centerGrid.add(blueColumn);
        centerGrid.add(yellowColumn);

        JScrollPane globalScrollPane = new JScrollPane(centerGrid);
        globalScrollPane.setBorder(null);
        globalScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(globalScrollPane, BorderLayout.CENTER);

        loadTasksFromController();
        printAllTasks("Инициализация");
    }

    private void printAllTasks(String action) {
        System.out.println("\n========== " + action + " ==========");
        List<Task> tasks = controller.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
        } else {
            System.out.println("Всего задач: " + tasks.size());
            for (Task task : tasks) {
                System.out.println("  ID: " + task.getId() +
                        " | Название: " + task.getTitle() +
                        " | Категория: " + task.getCategory());
            }
        }
        System.out.println("================================\n");
    }

    private void loadTasksFromController() {
        List<Task> tasks = controller.getAllTasks();
        for (Task task : tasks) {
            KanbanColumn column = categoryToColumn.get(task.getCategory());
            if (column != null) {
                column.addExistingBlock(task.getId(), task.getTitle());
            }
        }
    }

    public void refreshAllTasks() {
        for (KanbanColumn column : categoryToColumn.values()) {
            column.clearAllBlocks();
        }
        loadTasksFromController();
    }

    class KanbanColumn extends JPanel {
        private final JPanel contentPanel;
        private final String categoryName;

        public KanbanColumn(Color color, String name) {
            this.categoryName = name;
            setLayout(new BorderLayout());
            setOpaque(false);

            JPanel visualCard = new JPanel(new BorderLayout());
            visualCard.setBackground(color);
            visualCard.setBorder(new LineBorder(Color.GRAY, 1));

            JLabel header = new JLabel(name, SwingConstants.CENTER);
            header.setOpaque(true);
            header.setBackground(color.darker());
            header.setForeground(Color.WHITE);
            header.setBorder(new EmptyBorder(5, 5, 5, 5));
            header.setFont(new Font("Serif", Font.BOLD, 16));

            header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            header.setToolTipText("Нажмите для перехода в арену");
            header.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    gameWindow.getArenaPanel().loadCategory(categoryName);
                    gameWindow.showArena();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    header.setBackground(color.darker().darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    header.setBackground(color.darker());
                }
            });

            visualCard.add(header, BorderLayout.NORTH);

            contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(color);
            contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            visualCard.add(contentPanel, BorderLayout.CENTER);

            JButton addBtn = new JButton(" + Добавить блок");
            addBtn.setFocusPainted(false);
            addBtn.addActionListener((ActionEvent e) -> {
                String newTitle = "Запись #" + globalCounter++;
                controller.addTask(newTitle, categoryName);
                printAllTasks("Добавление задачи: " + newTitle);
                refreshAllTasks();
            });

            JPanel btnPanel = new JPanel(new BorderLayout());
            btnPanel.setBackground(color);
            btnPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            btnPanel.add(addBtn, BorderLayout.CENTER);
            visualCard.add(btnPanel, BorderLayout.SOUTH);

            add(visualCard, BorderLayout.NORTH);
        }

        public void addExistingBlock(int taskId, String text) {
            DraggableBlock block = new DraggableBlock(taskId, text);
            contentPanel.add(block);
            refreshUI();
        }

        public void acceptBlock(DraggableBlock block) {
            contentPanel.add(block);
            refreshUI();
        }

        public void clearAllBlocks() {
            contentPanel.removeAll();
            refreshUI();
        }

        private void refreshUI() {
            contentPanel.revalidate();
            contentPanel.repaint();
            if (getParent() != null) {
                if (getParent().getParent() != null) {
                    getParent().getParent().revalidate();
                }
            }
        }

        public String getCategoryName() {
            return categoryName;
        }
    }

    class DraggableBlock extends JPanel {
        private JTextField textField;
        private JCheckBox deleteCheckBox;
        private int taskId;
        private String originalText;

        public DraggableBlock(int taskId, String text) {
            this.taskId = taskId;
            this.originalText = text;

            setLayout(new BorderLayout());
            setBackground(Color.WHITE);
            setBorder(new CompoundBorder(
                    new EmptyBorder(0, 0, 10, 0),
                    new CompoundBorder(
                            new LineBorder(Color.LIGHT_GRAY, 1),
                            new EmptyBorder(10, 10, 10, 10)
                    )
            ));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            setAlignmentX(Component.CENTER_ALIGNMENT);

            deleteCheckBox = new JCheckBox();
            deleteCheckBox.setBackground(Color.WHITE);
            deleteCheckBox.setToolTipText("Отметить для удаления");
            deleteCheckBox.addActionListener(e -> {
                if (deleteCheckBox.isSelected()) {
                    deleteBlock();
                }
            });

            textField = new JTextField(text);
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setBackground(Color.WHITE);
            textField.setBorder(new EmptyBorder(5, 10, 5, 10));
            textField.setFont(new Font("SansSerif", Font.PLAIN, 14));

            textField.addActionListener(e -> updateTaskTitle());
            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    String currentText = textField.getText();
                    if (!currentText.equals(originalText)) {
                        updateTaskTitle();
                    }
                }
            });

            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            leftPanel.setBackground(Color.WHITE);
            leftPanel.add(deleteCheckBox);

            add(leftPanel, BorderLayout.WEST);
            add(textField, BorderLayout.CENTER);

            DragMouseAdapter listener = new DragMouseAdapter();
            addMouseListener(listener);
            addMouseMotionListener(listener);
        }

        private void updateTaskTitle() {
            String newTitle = textField.getText().trim();

            if (newTitle.equals(originalText) || newTitle.isEmpty()) {
                return;
            }

            controller.updateTask(taskId, newTitle);
            printAllTasks("Обновление текста задачи ID:" + taskId + " -> \"" + newTitle + "\"");
            originalText = newTitle;
        }

        private void deleteBlock() {
            controller.deleteTask(taskId);
            gameWindow.updateCharacterUI();
            printAllTasks("Удаление задачи ID:" + taskId);

            Container parent = this.getParent();
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();

                Container grandParent = parent.getParent();
                if (grandParent != null) {
                    grandParent.revalidate();
                    Container root = grandParent.getParent();
                    if (root != null) {
                        root.revalidate();
                    }
                }
            }
        }

        public String getText() {
            return textField.getText();
        }

        public void setText(String text) {
            textField.setText(text);
            originalText = text;
        }

        public int getTaskId() {
            return taskId;
        }

        private KanbanColumn findParentColumn(Component comp) {
            while (comp != null) {
                if (comp instanceof KanbanColumn) {
                    return (KanbanColumn) comp;
                }
                comp = comp.getParent();
            }
            return null;
        }
    }

    class DragMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                currentDraggingComponent = e.getComponent();
                pressPoint = e.getPoint();
                currentDraggingComponent.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e) || currentDraggingComponent == null) return;
            currentDraggingComponent.setCursor(Cursor.getDefaultCursor());

            if (pressPoint != null && pressPoint.distance(e.getPoint()) < 5) {
                currentDraggingComponent = null;
                return;
            }

            Point screenLoc = e.getLocationOnScreen();
            Component root = SwingUtilities.getRoot(currentDraggingComponent);
            if (root == null) return;

            Point rootPoint = new Point(screenLoc);
            SwingUtilities.convertPointFromScreen(rootPoint, root);

            Component target = SwingUtilities.getDeepestComponentAt(root, rootPoint.x, rootPoint.y);

            KanbanColumn targetColumn = findParentColumn(target);
            KanbanColumn sourceColumn = findParentColumn(currentDraggingComponent);

            if (targetColumn != null && targetColumn != sourceColumn) {
                DraggableBlock block = (DraggableBlock) currentDraggingComponent;
                int taskId = block.getTaskId();
                String newCategory = targetColumn.getCategoryName();

                controller.updateTaskCategory(taskId, newCategory);
                printAllTasks("Перемещение задачи ID:" + taskId + " в " + newCategory);
                refreshAllTasks();
            }

            currentDraggingComponent = null;
            pressPoint = null;
        }

        private KanbanColumn findParentColumn(Component comp) {
            while (comp != null) {
                if (comp instanceof KanbanColumn) {
                    return (KanbanColumn) comp;
                }
                comp = comp.getParent();
            }
            return null;
        }
    }
}