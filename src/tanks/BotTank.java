package tanks;

import game.Director;

import javax.swing.*;

public class BotTank extends JLabel implements GenericTank {
    private final int speed = 1;
    public static int reloadTime = 5;
    private String direction = "IDLE";

    public decideDirection() {
        if (!bL && !bU && bR && !bD)
            direction = "Right";

        else if (bL && !bU && !bR && !bD)
            direction = "Left";

        else if (!bL && bU && !bR && !bD)
            direction = "Uturn";

        else if (!bL && !bU && !bR && bD)
            direction = Direction.D;

        else if (!bL && !bU && !bR && !bD)
            direction = "Stop";
    }

    public TankBot(int x, int y, int Bot_ID) {
        super();
        this.Bot_ID = Bot_ID;

        setBounds(x, y, Random(64), Random(64);
        initPositionUpdater();
    }
    @Override
    public void stop() {
        direction = "IDLE";
    }