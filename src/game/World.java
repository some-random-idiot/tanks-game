package game;

import tanks.*;
import tanks.commands.*;
import tile.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private int p1Xinitial,  p1Yinitial, p2Xinitial, p2Yinitial;

    private final GenericTile[][] tileLayout = new GenericTile[initialLayout.length][initialLayout[0].length];

    private JFrame rootWindow;
    private JPanel rootPanel;

    private final String mode;

    private PlayerTank player1;
    private PlayerTank player2;
    private List<BotTank> botTanks = new ArrayList<>();

    private MoveUpCommand moveUpCommandP1, moveUpCommandP2;
    private MoveDownCommand moveDownCommandP1, moveDownCommandP2;
    private MoveLeftCommand moveLeftCommandP1, moveLeftCommandP2;
    private MoveRightCommand moveRightCommandP1, moveRightCommandP2;

    private List<TankShell> activeShells = new ArrayList<>();

    public World(String mode) {
        this.mode = mode;

        initWindow();
        initFrameWatcher();
        initLevel();
        setKeyBindings();
        spawnEnemies(Director.getInstance().wave);
        initShootAI();
        initCollisionChecker();

        addObserver(Director.getInstance());
    }

    private void initWindow() {
        // Set up game window.
        rootWindow = new JFrame("TANKS | Wave: " + Director.getInstance().wave);
        rootWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rootWindow.setSize(847, 803);
        rootWindow.setResizable(false);

        // Don't use layout managers. They make positioning stuff a headache.
        rootPanel = new JPanel(null);
        rootPanel.setBackground(Color.BLACK);

        rootWindow.add(rootPanel);
        rootWindow.setVisible(true);
    }

    private void initFrameWatcher() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (Director.getInstance().onGoing) {
                    if (!rootWindow.isVisible()) {
                        setChanged();
                        notifyObservers("GAMESPACE_CLOSED");
                    }
                }
            }
        };
        thread.start();
    }

    private void initLevel() {
        // Build the level from the initialLayout jagged array.
        for (int y = 0; y < initialLayout.length; y++) {
            for (int x = 0; x < initialLayout[y].length; x++) {
                GenericTile tile = null;
                if ("b".equals(initialLayout[y][x])) {
                    tile = new BrickTile();
                }
                if ("s".equals(initialLayout[y][x])) {
                    tile = new SteelTile();
                }
                if ("t".equals(initialLayout[y][x])) {
                    tile = new TreeTile();
                }
                if ("hq".equals(initialLayout[y][x])) {
                    tile = new HeadquarterTile();
                }
                if ("p1".equals(initialLayout[y][x])) {
                    p1Xinitial = x * 64;
                    p1Yinitial = y * 64;
                }
                if ("p2".equals(initialLayout[y][x])) {
                    p2Xinitial = x * 64;
                    p2Yinitial = y * 64;
                }

                if (tile != null) {
                    tileLayout[y][x] = tile;
                    tile.setBounds(x * 64, y * 64, 64, 64);
                    rootPanel.add(tile);
                }
            }
        }
        initPlayers();
        initCommands();
    }

    private void initPlayers() {
        if (mode.equals("mp")) {
            // If multiplayer, then create two tanks.
            player2 = new PlayerTank(p2Xinitial, p2Yinitial, 2);
            rootPanel.add(player2);
        }
        player1 = new PlayerTank(p1Xinitial, p1Yinitial, 1);
        rootPanel.add(player1);
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

    public void reset() {
        synchronized (botTanks) {
            for (BotTank tank : botTanks) {
                tank.setVisible(false);
                rootPanel.remove(tank);
                rootPanel.validate();
            }
            botTanks.clear();
        }

        List<TankShell> inactiveShells = new ArrayList<>(activeShells);
        synchronized (activeShells) {
            removeShell(inactiveShells);
        }

        resetTiles();
        resetPlayers();
        spawnEnemies(Director.getInstance().wave);
        initShootAI();
        initCollisionChecker();
        initFrameWatcher();
        rootWindow.setTitle("TANKS | Wave: " + Director.getInstance().wave);
    }

    private void resetTiles() {
        // Reset the level.
        for (GenericTile[] genericTiles : tileLayout) {
            for (GenericTile genericTile : genericTiles) {
                if (genericTile != null) {
                    genericTile.reset();
                }
            }
        }
    }

    private void resetPlayers() {
        // Reset the players.
        player1.resetOrientation();
        player1.setBounds(p1Xinitial, p1Yinitial, player1.getWidth(), player1.getHeight());
        player1.revive();
        if (mode.equals("mp")) {
            player2.resetOrientation();
            player2.setBounds(p2Xinitial, p2Yinitial, player2.getWidth(), player2.getHeight());
            player2.revive();
        }
    }

    private void setKeyBindings() {
        // Set up key bindings.
        rootWindow.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (Director.getInstance().onGoing) {
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                        moveUpCommandP1.execute();
                    } else if (e.getKeyCode() == KeyEvent.VK_S) {
                        moveDownCommandP1.execute();
                    } else if (e.getKeyCode() == KeyEvent.VK_A) {
                        moveLeftCommandP1.execute();
                    } else if (e.getKeyCode() == KeyEvent.VK_D) {
                        moveRightCommandP1.execute();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        createShell(player1, "FRIENDLY");
                    }
                }
            }
        });

        if (mode.equals("mp")) {
            rootWindow.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (Director.getInstance().onGoing) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            moveUpCommandP2.execute();
                        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            moveDownCommandP2.execute();
                        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            moveLeftCommandP2.execute();
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            moveRightCommandP2.execute();
                        }
                        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                            createShell(player2, "FRIENDLY");
                        }
                    }
                }
            });
        }
    }

    public void spawnEnemies(int wave) {
        // Spawn enemies.
        for (int i = 0; i < initialLayout.length; i++) {
            for (int j = 0; j < initialLayout[i].length; j++) {
                if (Objects.equals(initialLayout[i][j], "x")) {
                    BotTank botTank = new BotTank(j * 64, i * 64);
                    synchronized (botTanks) {
                        botTanks.add(botTank);
                    }
                    rootPanel.add(botTank);
                    wave--;
                }
                if (wave == 0) {
                    return;
                }
            }
        }
    }

    private void initShootAI() {
        // Make enemies shoot in an interval.
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (Director.getInstance().onGoing) {
                    synchronized (botTanks) {
                        for (BotTank botTank : botTanks) {
                            createShell(botTank, "ENEMY");
                        }
                    }

                    // Need to give shoot AI a delay to prevent rapid shooting bug.
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    private void createShell(GenericTank tank, String friendly) {
        // Check tank icon instead of tank direction for bullet orientation.
        if (!tank.reloading && tank.isAlive) {
            String direction;
            Icon tankIcon = tank.getIcon();
            if (tankIcon.toString().contains("Up")) {
                direction = "UP";
            } else if (tankIcon.toString().contains("Down")) {
                direction = "DOWN";
            } else if (tankIcon.toString().contains("Left")) {
                direction = "LEFT";
            } else {
                direction = "RIGHT";
            }

            TankShell shell = new TankShell(tank.getX(), tank.getY());
            shell.setDirection(direction);

            if (friendly.equals("FRIENDLY")) {
                shell.setFriendly(true);
            }
            else {
                // NEED TO EXPLICITLY SET TO FALSE SINCE WE ARE REUSING BULLETS.
                shell.setFriendly(false);
            }

            rootPanel.add(shell);

            synchronized (activeShells) {
                activeShells.add(shell);
            }

            shell.initBallistic();
            reloadTank(tank);
        }
    }

    private void removeShell(List<TankShell> inactiveShells) {
        for (TankShell inactiveShell : inactiveShells) {
            inactiveShell.setVisible(false);
            activeShells.remove(inactiveShell);
            rootPanel.remove(inactiveShell);
            rootPanel.validate();
        }
    }

    private void reloadTank(GenericTank tank) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                tank.reloading = true;

                try {
                    Thread.sleep(1000L * GenericTank.reloadTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                tank.reloading = false;
            }
        };
        thread.start();
    }

    private void initCollisionChecker() {
        // There has to be a better way to check collision between objects, but we don't have the time for that.
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (Director.getInstance().onGoing) {
                    checkBorderCollision();
                    checkTileCollision();
                    checkTankRamTank();
                    checkTankShot();
                }
            }
        };
        thread.start();
    }

    private void checkBorderCollision() {
        if(player1.getX() > rootPanel.getWidth() - 57 ||
           player1.getY() > rootPanel.getHeight() - 57 ||
           player1.getX() < 0 || player1.getY() < 0)
        {
            stopTankCollided(player1.direction, player1);
        }

        if (mode.equals("mp")) {
            if (player2.getX() > rootPanel.getWidth() - 57 ||
                player2.getY() > rootPanel.getHeight() - 57 ||
                player2.getX() < 0 || player2.getY() < 0)
            {
                stopTankCollided(player2.direction, player2);
            }
        }
        synchronized (botTanks) {
            for (BotTank tank : botTanks) {
                if (tank.getX() > rootPanel.getWidth() - 57 ||
                        tank.getY() > rootPanel.getHeight() - 57 ||
                        tank.getX() < 0 || tank.getY() < 0) {
                    stopTankCollided(tank.direction, tank);
                }
            }
        }

        List<TankShell> inactiveShells = new ArrayList<>();
        synchronized (activeShells) {
            for (TankShell shell : activeShells) {
                if (shell.getX() > rootPanel.getWidth() - 57 ||
                        shell.getY() > rootPanel.getHeight() - 57 ||
                        shell.getX() < 0 || shell.getY() < 0) {
                    inactiveShells.add(shell);
                }
            }
            removeShell(inactiveShells);
        }
    }

    private void checkTileCollision() {
        for (int i = 0; i < tileLayout.length; i++) {
            for (int j = 0; j < tileLayout[i].length; j++) {
                if (tileLayout[i][j] != null) {
                    checkTankTileCollision(i, j, player1);
                    if (Objects.equals(mode, "mp")) {
                        checkTankTileCollision(i, j, player2);
                    }
                    synchronized (botTanks) {
                        for (BotTank tank : botTanks) {
                            checkTankTileCollision(i, j, tank);
                        }
                    }
                    checkShellTileCollision(i, j);
                }
            }
        }
    }

    private void checkTankTileCollision(int i, int j, GenericTank tank) {
        // Checks the collision between a tank and a tile.
        if (tank.getBounds().intersects(tileLayout[i][j].getBounds()) && tileLayout[i][j].isSolid)
        {
            String direction = tank.direction;
            stopTankCollided(direction, tank);
        }
    }

    private void checkShellTileCollision(int i, int j) {
        // Checks the collision between a shell and a tile.
        List<TankShell> inactiveShells = new ArrayList<>();

        synchronized (activeShells) {
            for (TankShell shell : activeShells) {
                if (shell.getBounds().intersects(tileLayout[i][j].getBounds()) && tileLayout[i][j].isSolid) {
                    inactiveShells.add(shell);
                    tileLayout[i][j].destroy();

                    if (tileLayout[i][j] instanceof HeadquarterTile) {
                        setChanged();
                        notifyObservers("GAME_OVER");
                    }
                }
            }
            removeShell(inactiveShells);
        }
    }

    private void stopTankCollided(String direction, GenericTank tank) {
        tank.brake();
        switch (direction) {
            case "UP" -> tank.setBounds(tank.getX(), tank.getY() + 3, tank.getWidth(), tank.getHeight());
            case "DOWN" -> tank.setBounds(tank.getX(), tank.getY() - 3, tank.getWidth(), tank.getHeight());
            case "LEFT" -> tank.setBounds(tank.getX() + 3, tank.getY(), tank.getWidth(), tank.getHeight());
            case "RIGHT" -> tank.setBounds(tank.getX() - 3, tank.getY(), tank.getWidth(), tank.getHeight());
        }
    }

    private void checkTankRamTank() {
        // Checks the collision between two player tanks.
        if (mode.equals("mp")) {
            if (player1.getBounds().intersects(player2.getBounds())) {
                stopTankCollided(player1.direction, player1);
                stopTankCollided(player2.direction, player2);
            }
            synchronized (botTanks) {
                for (BotTank tank : botTanks) {
                    if (player2.getBounds().intersects(tank.getBounds())) {
                        stopTankCollided(player2.direction, player2);
                        stopTankCollided(tank.direction, tank);
                    }
                }
            }
        }
        // Checks the collision between a player 1 and a bot tank.
        synchronized (botTanks) {
            for (BotTank tank : botTanks) {
                if (player1.getBounds().intersects(tank.getBounds())) {
                    stopTankCollided(player1.direction, player1);
                    stopTankCollided(tank.direction, tank);
                }
            }
            // Check collision between a bot tank and a bot tank.
            for (int i = 0; i < botTanks.size(); i++) {
                for (int j = i + 1; j < botTanks.size(); j++) {
                    if (botTanks.get(i).getBounds().intersects(botTanks.get(j).getBounds())) {
                        stopTankCollided(botTanks.get(i).direction, botTanks.get(i));
                        stopTankCollided(botTanks.get(j).direction, botTanks.get(j));
                    }
                }
            }
        }
    }

     private void checkTankShot() {
        List<TankShell> inactiveShells = new ArrayList<>();
        synchronized (activeShells) {
            for (TankShell shell : activeShells) {
                if (mode.equals("mp")) {
                    if (!shell.friendly) {
                        if (player1.getBounds().intersects(shell.getBounds())) {
                            player1.destroy();
                            inactiveShells.add(shell);
                        }
                        if (player2.getBounds().intersects(shell.getBounds())) {
                            player2.destroy();
                            inactiveShells.add(shell);
                        }
                    }
                    if (!player1.isAlive && !player2.isAlive) {
                        setChanged();
                        notifyObservers("GAME_OVER");
                        inactiveShells.add(shell);
                    }
                }
                else {
                    if (shell.getBounds().intersects(player1.getBounds()) && !shell.friendly) {
                        player1.destroy();
                        inactiveShells.add(shell);
                        setChanged();
                        notifyObservers("GAME_OVER");
                    }
                }
                synchronized (botTanks) {
                    for (BotTank tank : botTanks) {
                        if (shell.getBounds().intersects(tank.getBounds())) {
                            if (shell.friendly) {
                                tank.destroy();
                                shell.setInactive();
                                setChanged();
                                notifyObservers("BOT_TANK_DESTROYED");
                            }
                        }
                    }
                }
            }
            removeShell(inactiveShells);
        }

        boolean enemiesLeft = false;
        synchronized (botTanks) {
            for (BotTank tank : botTanks) {
                if (tank.isAlive) {
                    enemiesLeft = true;
                    break;
                }
            }
        }
        if (!enemiesLeft) {
            setChanged();
            notifyObservers("WAVE_CLEARED");
        }
    }
}
