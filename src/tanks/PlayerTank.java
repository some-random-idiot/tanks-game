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

    }

    @Override
    public void fire() {

    }
}
