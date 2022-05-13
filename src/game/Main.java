package game;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static void initMenu() {
        JFrame menuWindow = new JFrame("Welcome to TANKS!");
        menuWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuWindow.setSize(500, 500);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(3, 1));

        JButton singleplayerButton = new JButton("Singleplayer");
        JButton multiplayerButton = new JButton("Multiplayer");
        JButton exitButton = new JButton("Exit");

        menuPanel.add(singleplayerButton);
        menuPanel.add(multiplayerButton);
        menuPanel.add(exitButton);

        menuWindow.add(menuPanel);
        menuWindow.setVisible(true);
    }

    public static void main(String[] args) {
        initMenu();

        Director director = new Director();
        director.start();

    }
}
