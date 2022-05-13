package game;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class World extends Observable {
    private final String[][] initialLayout = {
            // x = nothing, b = brick, s = steel, t = tree, hq = base
            new String[] {"x", "x", "x", "x", "b", "b", "x", "x", "b", "x", "x", "x", "x"}, // 1
            new String[] {"x", "t", "t", "b", "b", "x", "x", "x", "b", "x", "x", "x", "x"}, // 2
            new String[] {"t", "t", "t", "t", "t", "t", "t", "t", "b", "b", "x", "x", "x"}, // 3
            new String[] {"t", "s", "b", "t", "b", "b", "b", "t", "t", "t", "t", "b", "s"}, // 4
            new String[] {"t", "t", "b", "t", "t", "t", "s", "t", "t", "b", "s", "b", "x"}, // 5
            new String[] {"x", "t", "t", "b", "s", "t", "t", "t", "t", "b", "x", "b", "x"}, // 6
            new String[] {"x", "b", "b", "b", "b", "b", "t", "t", "b", "b", "b", "t", "t"}, // 7
            new String[] {"s", "s", "x", "x", "x", "x", "x", "b", "b", "x", "x", "x", "t"}, // 8
            new String[] {"x", "b", "b", "b", "x", "s", "b", "b", "t", "t", "b", "b", "t"}, // 9
            new String[] {"x", "b", "x", "x", "b", "b", "b", "t", "t", "b", "x", "x", "t"}, // 10
            new String[] {"x", "x", "b", "x", "t", "b", "b", "b", "b", "t", "b", "t", "t"}, // 11
            new String[] {"x", "x", "b", "x", "x", "b", "hq", "b", "x", "t", "t", "t", "x"}, // 12
    };
    private JFrame rootWindow;
    private JPanel rootPanel;

    public void initWorld() {
        // Set up game window.
        rootWindow = new JFrame("TANKS");
        rootWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rootWindow.setSize(847, 803);

        // Don't use layout managers. They make positioning stuff a headache.
        rootPanel = new JPanel(null);
        rootPanel.setBackground(Color.BLACK);

        buildLevel();

        rootWindow.add(rootPanel);
        rootWindow.setVisible(true);
    }

    private void buildLevel() {
        for (int y = 0; y < initialLayout.length; y++) {
            for (int x = 0; x < initialLayout[y].length; x++) {
                System.out.println(initialLayout[y][x]);
                JLabel tile = new JLabel();
                if ("b".equals(initialLayout[y][x])) {
                    tile = new JLabel(new ImageIcon("./asset/sprite/brick.png"));
                }
                if ("s".equals(initialLayout[y][x])) {
                    tile = new JLabel(new ImageIcon("./asset/sprite/steel.png"));
                }
                if ("t".equals(initialLayout[y][x])) {
                    tile = new JLabel(new ImageIcon("./asset/sprite/tree.png"));
                }
                if ("hq".equals(initialLayout[y][x])) {
                    tile = new JLabel(new ImageIcon("./asset/sprite/base.png"));
                }
                tile.setBounds(x * 64, y * 64, 64, 64);
                rootPanel.add(tile);
            }
        }
    }
}
