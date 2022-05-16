package tanks;

import game.Director;

import javax.swing.*;

public abstract class GenericTank extends JLabel {
    public int speed = 1;
    public static int reloadTime = 3; // In seconds.
    public String direction = "IDLE";
    public boolean reloading = false;
    public boolean isAlive = true;

    public void initPositionUpdater() {
        Thread thread = new Thread() {
            // Update the position of the tank.
            @Override
            public void run() {
                while (isAlive) {
                    switch (direction) {
                        case "UP" -> setBounds(getX(), getY() - speed, getWidth(), getWidth());
                        case "DOWN" -> setBounds(getX(), getY() + speed, getWidth(), getWidth());
                        case "LEFT" -> setBounds(getX() - speed, getY(), getWidth(), getWidth());
                        case "RIGHT" -> setBounds(getX() + speed, getY(), getWidth(), getWidth());
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

    public void moveUp() {

    }

    public void moveDown() {

    }

    public void moveLeft() {

    }

    public void moveRight() {

    }

    public void brake() {
        direction = "IDLE";
    }

    public void resetOrientation() {

    }

    public void destroy() {
        isAlive = false;
        setBounds(getX(), getY() + 999, getWidth(), getHeight()); // Yeet off the screen.
    }
}
