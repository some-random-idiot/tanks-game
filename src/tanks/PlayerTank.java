package tanks;

import game.Director;

import javax.swing.*;

public class PlayerTank extends JLabel implements GenericTank {
    private final int playerNumber;
    private final int speed = 1;
    private String direction = "IDLE";

    public String getDirection() {
        return direction;
    }

    public PlayerTank(int x, int y, int playerNumber) {
        super();
        this.playerNumber = playerNumber;

        switch (playerNumber) {
            case 1 -> setIcon(TankSprites.player1SpriteUp);
            case 2 -> setIcon(TankSprites.player2SpriteUp);
        }

        setBounds(x, y, 64, 64);
        initPositionUpdater();
    }

    @Override
    public void initPositionUpdater() {
        Thread thread = new Thread() {
            // Update the position of the tank.
            @Override
            public void run() {
                while (true) {
                    switch (direction) {
                        case "UP" -> setBounds(getX(), getY() - speed, 64, 64);
                        case "DOWN" -> setBounds(getX(), getY() + speed, 64, 64);
                        case "LEFT" -> setBounds(getX() - speed, getY(), 64, 64);
                        case "RIGHT" -> setBounds(getX() + speed, getY(), 64, 64);
                    }

                    try {
                        Thread.sleep(Director.frameRate);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void moveUp() {
        if (direction.equals("DOWN")) {
            stop();
            return;
        }

        if (playerNumber == 1) {
            setIcon(TankSprites.player1SpriteUp);
        }
        else {
            setIcon(TankSprites.player2SpriteUp);
        }
        direction = "UP";
    }

    @Override
    public void moveDown() {
        if (direction.equals("UP")) {
            stop();
            return;
        }

        if (playerNumber == 1) {
            setIcon(TankSprites.player1SpriteDown);
        }
        else {
            setIcon(TankSprites.player2SpriteDown);
        }
        direction = "DOWN";
    }

    @Override
    public void moveLeft() {
        if (direction.equals("RIGHT")) {
            stop();
            return;
        }

        if (playerNumber == 1) {
            setIcon(TankSprites.player1SpriteLeft);
        }
        else {
            setIcon(TankSprites.player2SpriteLeft);
        }
        direction = "LEFT";
    }

    @Override
    public void moveRight() {
        if (direction.equals("LEFT")) {
            stop();
            return;
        }

        if (playerNumber == 1) {
            setIcon(TankSprites.player1SpriteRight);
        }
        else {
            setIcon(TankSprites.player2SpriteRight);
        }
        direction = "RIGHT";
    }

    @Override
    public void stop() {
        direction = "IDLE";
    }


    @Override
    public void shoot() {
        TankShell shell = new TankShell(getX(), getY(), direction);
    }
}
