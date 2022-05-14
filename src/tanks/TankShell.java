package tanks;

import game.Director;

import javax.swing.*;

public class TankShell extends JLabel {
    private final int speed = 2;
    private final String direction;
    public boolean friendly = false;

    public TankShell(int x, int y, String direction, String friendly) {
        super();
        switch (direction) {
            case "UP" -> setIcon(TankSprites.shellSpriteUp);
            case "DOWN" -> setIcon(TankSprites.shellSpriteDown);
            case "LEFT" -> setIcon(TankSprites.shellSpriteLeft);
            case "RIGHT" -> setIcon(TankSprites.shellSpriteRight);
        }
        setBounds(x, y, 64, 64);
        this.direction = direction;

        if (friendly.equals("FRIENDLY")) {
            this.friendly = true;
        }

        initBallistic();
    }

    public void initBallistic() {
        Thread thread = new Thread() {
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
}
