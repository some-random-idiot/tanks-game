package game;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final Director director = new Director();

    private static void initMenu() {
        JFrame menuWindow = new JFrame("Welcome to TANKS!");
        menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuWindow.setSize(500, 500);
        menuWindow.setLayout(new BorderLayout());
        menuWindow.setResizable(false);

        JLabel titleText = new JLabel("TANKS");
        titleText.setFont(new Font("Courier New", Font.BOLD, 80));
        titleText.setHorizontalAlignment(JLabel.CENTER);
        menuWindow.add(titleText, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1));

        JButton singleplayerButton = new JButton("Singleplayer");
        singleplayerButton.addActionListener(e -> {
            director.start("sp");
        }
        );

        JButton multiplayerButton = new JButton("Multiplayer");
        multiplayerButton.addActionListener(e -> {
                    director.start("mp");
                }
        );

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            System.exit(0);
        }
        );

        menuPanel.add(singleplayerButton);
        menuPanel.add(multiplayerButton);
        menuPanel.add(exitButton);

        menuWindow.add(menuPanel, BorderLayout.CENTER);
        menuWindow.setVisible(true);
    }

    public static void main(String[] args) {
        initMenu();
    }
}
