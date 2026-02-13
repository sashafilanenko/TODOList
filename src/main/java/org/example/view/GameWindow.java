package org.example.view;

import org.example.controller.TaskController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class GameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MapPanel mapPanel;
    private ArenaPanel arenaPanel;
    private HallPanel hallPanel;
    private HistoryPannel historyPanel;

    private TaskController controller;

    public GameWindow(TaskController controller){
        this.controller =  controller;

        setTitle("ммм");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(200, 0));

        Dimension btnSize = new Dimension(180, 60);

        JButton btnMap = new JButton("Карта");
        JButton btnArena = new JButton("Арена");
        JButton btnHall = new JButton("Зал славы");
        JButton btnHis = new JButton("История");

        JTextField textLVL = new JTextField();
        textLVL.setText("Level 6");
        textLVL.setEditable(false);

        for (JButton btn : new JButton[]{btnMap, btnArena, btnHall, btnHis}) {
            btn.setMaximumSize(btnSize);
            btn.setPreferredSize(btnSize);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(btnMap);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(btnArena);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(btnHall);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(btnHis);


        ImageIcon avatarIcon = new ImageIcon(
                getClass().getResource("/avatar.png")
        );

        AvatarPanel avatarPanel = new AvatarPanel(avatarIcon.getImage(), 120);
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(avatarPanel);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(textLVL);

        JPanel strikeBar = new JPanel();
        JTextField strikeCount = new JTextField();
        strikeCount.setText("5");
        strikeCount.setEditable(false);

        ImageIcon fireImg1 = new ImageIcon(
                getClass().getResource("/fire.png")
        );
        AvatarPanel fireImg = new AvatarPanel(fireImg1.getImage(), 20);
        fireImg.setAlignmentX(Component.CENTER_ALIGNMENT);

        strikeBar.add(fireImg);
        strikeBar.add(strikeCount);

        menuPanel.add(strikeBar);

        mapPanel = new MapPanel(controller);
        arenaPanel = new ArenaPanel();
        hallPanel = new HallPanel();
        historyPanel = new HistoryPannel();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(mapPanel, "MAP");
        mainPanel.add(arenaPanel, "ARENA");
        mainPanel.add(hallPanel, "HALL");
        mainPanel.add(historyPanel, "History");

        btnMap.addActionListener(e -> cardLayout.show(mainPanel, "MAP"));
        btnArena.addActionListener(e -> cardLayout.show(mainPanel, "ARENA"));
        btnHall.addActionListener(e -> cardLayout.show(mainPanel, "HALL"));
        btnHis.addActionListener(e -> cardLayout.show(mainPanel, "History"));

        add(menuPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);


        JPanel progressPanel = new JPanel(new BorderLayout());
        JTextField progress = new JTextField();
        progress.setText("0/250");
        progress.setEditable(false);
        progress.setHorizontalAlignment(JTextField.CENTER);

        HealthBar healthBar = new HealthBar();

        progressPanel.add(healthBar, BorderLayout.CENTER);
        progressPanel.add(progress, BorderLayout.EAST);

        add(progressPanel, BorderLayout.SOUTH);

        /*
        new Timer(500, e -> {
            int newHealth = (int)(Math.random() * 101);
            healthBar.setHealth(newHealth);
        }).start();

         */
    }


    private static class AvatarPanel extends JPanel {

        private final Image image;
        private final int size;

        public AvatarPanel(Image image, int size) {
            this.image = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            this.size = size;
            setPreferredSize(new Dimension(size, size));
            setMinimumSize(new Dimension(size, size));
            setMaximumSize(new Dimension(size, size));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Shape circle = new Ellipse2D.Double(0, 0, size, size);
            g2.setClip(circle);

            g2.drawImage(image, 0, 0, this);

            g2.dispose();
        }
    }


    private static class HealthBar extends JPanel {
        private int health = 100;

        public HealthBar() {
            setPreferredSize(new Dimension(0, 20));
            setBackground(Color.DARK_GRAY);
        }

        public void setHealth(int health) {
            this.health = Math.max(0, Math.min(100, health));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int width = getWidth();
            int height = getHeight();

            g.setColor(Color.RED);
            g.fillRect(0, 0, width * health / 100, height);

            g.setColor(Color.BLACK);
            g.drawRect(0, 0, width - 1, height - 1);
        }
    }

}
