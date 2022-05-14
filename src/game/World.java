package game;

import tanks.PlayerTank;
import tanks.TankShell;
import tanks.TankSprites;
import tanks.commands.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;

public class World extends Observable {
    private final String[][] initialLayout = {
            // x = nothing, b = brick, s = steel, t = tree, hq = base, p1 = player 1, p2 = player 2
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
            new String[] {"x", "x", "b", "x", "p1", "b", "hq", "b", "p2", "t", "t", "t", "x"}, // 12
    };
    private JFrame rootWindow;
    private JPanel rootPanel;

    private final String mode;

    private PlayerTank player1;
    private PlayerTank player2;

    private MoveUpCommand moveUpCommandP1, moveUpCommandP2;
    private MoveDownCommand moveDownCommandP1, moveDownCommandP2;
    private MoveLeftCommand moveLeftCommandP1, moveLeftCommandP2;
    private MoveRightCommand moveRightCommandP1, moveRightCommandP2;

    boolean canShootP1 = true, canShootP2 = true;

    public World(String mode) {
        this.mode = mode;

        initWindow();
        initLevel();
        setKeyBindings();

        rootWindow.add(rootPanel);
        rootWindow.setVisible(true);
    }

    private void initWindow() {
        // Set up game window.
        rootWindow = new JFrame("TANKS");
        rootWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rootWindow.setSize(847, 803);
        rootWindow.setResizable(false);

        // Don't use layout managers. They make positioning stuff a headache.
        rootPanel = new JPanel(null);
        rootPanel.setBackground(Color.BLACK);
    }

    private void initLevel() {
        // Build the level from the initialLayout jagged array.
        int p1X = 0;
        int p1Y = 0;
        int p2X = 0;
        int p2Y = 0;

        for (int y = 0; y < initialLayout.length; y++) {
            for (int x = 0; x < initialLayout[y].length; x++) {
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
                if ("p1".equals(initialLayout[y][x])) {
                    p1X = x * 64;
                    p1Y = y * 64;
                }
                if ("p2".equals(initialLayout[y][x])) {
                    p2X = x * 64;
                    p2Y = y * 64;
                }
                tile.setBounds(x * 64, y * 64, 64, 64);
                rootPanel.add(tile);
            }
        }

        if (mode.equals("mp")) {
            // If multiplayer, then create two tanks.
            player2 = new PlayerTank(p2X, p2Y, 2);
            rootPanel.add(player2);
        }
        player1 = new PlayerTank(p1X, p1Y, 1);
        rootPanel.add(player1);

        initCommands();
    }

    private void initCommands() {
        moveUpCommandP1 = new MoveUpCommand(player1);
        moveDownCommandP1 = new MoveDownCommand(player1);
        moveLeftCommandP1 = new MoveLeftCommand(player1);
        moveRightCommandP1 = new MoveRightCommand(player1);

        if (mode.equals("mp")) {
            moveUpCommandP2 = new MoveUpCommand(player2);
            moveDownCommandP2 = new MoveDownCommand(player2);
            moveLeftCommandP2 = new MoveLeftCommand(player2);
            moveRightCommandP2 = new MoveRightCommand(player2);
        }
    }

    private void setKeyBindings() {
        // Set up key bindings.
        rootWindow.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    moveUpCommandP1.execute();
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    moveDownCommandP1.execute();
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    moveLeftCommandP1.execute();
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    moveRightCommandP1.execute();
                }

                if (e.getKeyCode() == KeyEvent.VK_SHIFT && canShootP1) {
                    // Check tank icon instead of tank direction for bullet orientation.
                    String direction;
                    Icon tankIcon = player1.getIcon();
                    if (tankIcon == TankSprites.player1SpriteUp) {
                        direction = "UP";
                    }
                    else if (tankIcon == TankSprites.player1SpriteDown) {
                        direction = "DOWN";
                    }
                    else if (player1.getIcon() == TankSprites.player1SpriteLeft) {
                        direction = "LEFT";
                    }
                    else {
                        direction = "RIGHT";
                    }

                    TankShell shell = new TankShell(player1.getX(), player1.getY(), direction, "FRIENDLY");
                    rootPanel.add(shell);
                    reload(1);
                }
            }
        });

        if (mode.equals("mp")) {
            rootWindow.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        moveUpCommandP2.execute();
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        moveDownCommandP2.execute();
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        moveLeftCommandP2.execute();
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        moveRightCommandP2.execute();
                    }

                    if (e.getKeyCode() == KeyEvent.VK_SPACE && canShootP2) {
                        // Check tank icon instead of tank direction for bullet orientation.
                        String direction;
                        Icon tankIcon = player2.getIcon();
                        if (tankIcon == TankSprites.player2SpriteUp) {
                            direction = "UP";
                        }
                        else if (tankIcon == TankSprites.player2SpriteDown) {
                            direction = "DOWN";
                        }
                        else if (player2.getIcon() == TankSprites.player2SpriteLeft) {
                            direction = "LEFT";
                        }
                        else {
                            direction = "RIGHT";
                        }

                        TankShell shell = new TankShell(player2.getX(), player2.getY(), direction, "FRIENDLY");
                        rootPanel.add(shell);
                        reload(2);
                    }
                }
            });
        }
    }

    private void reload(int playerNumber) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                if (playerNumber == 1) {
                    canShootP1 = false;
                }
                else {
                    canShootP2 = false;
                }
                try {
                    Thread.sleep(1000 * PlayerTank.reloadTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (playerNumber == 1) {
                    canShootP1 = true;
                }
                else {
                    canShootP2 = true;
                }
            }
        };
        thread.start();
    }
}
