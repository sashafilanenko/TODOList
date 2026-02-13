package org.example.view;

import org.example.controller.TaskController;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapPanel extends JPanel {

    private static Component currentDraggingComponent = null;
    private static Point pressPoint = null;

    private TaskController controller;
    private int globalCounter = 1;

    public MapPanel(TaskController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("Карта битвы", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);


        JPanel centerGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        centerGrid.setBackground(new Color(230, 230, 230));
        centerGrid.setBorder(new EmptyBorder(10, 10, 10, 10));


        centerGrid.add(new KanbanColumn(new Color(255, 220, 220), "Красная зона"));
        centerGrid.add(new KanbanColumn(new Color(220, 255, 220), "Зеленая зона"));
        centerGrid.add(new KanbanColumn(new Color(220, 220, 255), "Синяя зона"));
        centerGrid.add(new KanbanColumn(new Color(255, 255, 220), "Желтая зона"));


        JScrollPane globalScrollPane = new JScrollPane(centerGrid);
        globalScrollPane.setBorder(null);
        globalScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(globalScrollPane, BorderLayout.CENTER);
    }


    class KanbanColumn extends JPanel {
        private final JPanel contentPanel;

        public KanbanColumn(Color color, String name) {

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
            visualCard.add(header, BorderLayout.NORTH);

            contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(color);
            contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            visualCard.add(contentPanel, BorderLayout.CENTER);

            JButton addBtn = new JButton(" + Добавить блок");
            addBtn.setFocusPainted(false);
            addBtn.addActionListener((ActionEvent e) -> {
                addNewBlock("Запись #" + globalCounter++);
            });

            JPanel btnPanel = new JPanel(new BorderLayout());
            btnPanel.setBackground(color);
            btnPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            btnPanel.add(addBtn, BorderLayout.CENTER);
            visualCard.add(btnPanel, BorderLayout.SOUTH);

            add(visualCard, BorderLayout.NORTH);

            addNewBlock("Стартовый юнит");
        }

        public void addNewBlock(String text) {
            DraggableBlock block = new DraggableBlock(text);
            contentPanel.add(block);
            refreshUI();
        }

        public void acceptBlock(DraggableBlock block) {
            contentPanel.add(block);
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
    }

    class DraggableBlock extends JPanel {
        private JTextField textField;
        private JCheckBox deleteCheckBox;
        private java.util.List<String> tasks; // Список задач для этого блока

        public DraggableBlock(String text) {
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

            // Инициализируем список задач
            tasks = new java.util.ArrayList<>();
            tasks.add("Подзадача 1");
            tasks.add("Подзадача 2");
            tasks.add("Подзадача 3");

            // Чекбокс для удаления
            deleteCheckBox = new JCheckBox();
            deleteCheckBox.setBackground(Color.WHITE);
            deleteCheckBox.setToolTipText("Отметить для удаления");
            deleteCheckBox.addActionListener(e -> {
                if (deleteCheckBox.isSelected()) {
                    deleteBlock();
                }
            });

            // Текстовое поле
            textField = new JTextField(text);
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setBackground(Color.WHITE);
            textField.setBorder(new EmptyBorder(5, 10, 5, 10));
            textField.setFont(new Font("SansSerif", Font.PLAIN, 14));

            // Панель для чекбокса
            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            leftPanel.setBackground(Color.WHITE);
            leftPanel.add(deleteCheckBox);

            add(leftPanel, BorderLayout.WEST);
            add(textField, BorderLayout.CENTER);

            // Drag-and-drop слушатель
            DragMouseAdapter listener = new DragMouseAdapter();
            addMouseListener(listener);
            addMouseMotionListener(listener);
        }

        private void deleteBlock() {
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
        }

        // Получить список задач
        public java.util.List<String> getTasks() {
            return tasks;
        }

        // Добавить задачу
        public void addTask(String task) {
            tasks.add(task);
        }

        // Удалить задачу
        public void removeTask(int index) {
            if (index >= 0 && index < tasks.size()) {
                tasks.remove(index);
            }
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
                Container oldParent = currentDraggingComponent.getParent();
                if (oldParent != null) {
                    oldParent.remove(currentDraggingComponent);
                    oldParent.revalidate();
                    oldParent.repaint();
                }
                targetColumn.acceptBlock((DraggableBlock) currentDraggingComponent);

                SwingUtilities.getWindowAncestor(targetColumn).revalidate();
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