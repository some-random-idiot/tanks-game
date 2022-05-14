package tanks;

import javax.swing.*;

public class PlayerTank extends JLabel implements GenericTank {
    public PlayerTank(int x, int y, int playerNumber) {
        super();
        switch (playerNumber) {
            case 1 -> setIcon(new ImageIcon("./asset/sprite/playerTankA.png"));
            case 2 -> setIcon(new ImageIcon("./asset/sprite/playerTankB.png"));
        }
        setBounds(x, y, 64, 64);
    }

    @Override
    public void move() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    setBounds(getX() + 1, getY(), 64, 64);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    public void fire() {

    }
}
