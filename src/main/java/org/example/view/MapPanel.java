package org.example.view;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel {
    public MapPanel(){

        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255));

        JLabel title = new JLabel("Карта битвы", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

    }
}
