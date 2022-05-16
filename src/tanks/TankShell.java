package tanks;

import game.Director;

import javax.swing.*;

public class TankShell extends JLabel {
    private final int speed = 2;
    private boolean isActive = false;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        switch (direction) {
            case "UP" -> setIcon(TankSprites.shellSpriteUp);
            case "DOWN" -> setIcon(TankSprites.shellSpriteDown);
            case "LEFT" -> setIcon(TankSprites.shellSpriteLeft);
            case "RIGHT" -> setIcon(TankSprites.shellSpriteRight);
        }
        this.direction = direction;
    }

    private String direction;

    public boolean isFriendly() {
        return friendly;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    public boolean friendly = false;

    public TankShell(int x, int y) {
        super();
        setBounds(x, y, 60, 60);
    }

    public void initBallistic() {
        isActive = true;
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (isActive) {
                    switch (direction) {
                        case "UP" -> setBounds(getX(), getY() - speed, getWidth(), getHeight());
                        case "DOWN" -> setBounds(getX(), getY() + speed, getWidth(),  getHeight());
                        case "LEFT" -> setBounds(getX() - speed, getY(), getWidth(),  getHeight());
                        case "RIGHT" -> setBounds(getX() + speed, getY(), getWidth(),  getHeight());
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

    public void setInactive() {
        isActive = false;
        setBounds(-999, -999, getWidth(), getHeight());
    }
}
