package tanks;

public class PlayerTank extends GenericTank {
    private final int playerNumber;

    public PlayerTank(int x, int y, int playerNumber) {
        super();
        this.playerNumber = playerNumber;

        switch (playerNumber) {
            case 1 -> setIcon(TankSprites.player1SpriteUp);
            case 2 -> setIcon(TankSprites.player2SpriteUp);
        }

        setBounds(x, y, 62, 62);
        initPositionUpdater();
    }

    @Override
    public void moveUp() {
        if (direction.equals("DOWN")) {
            brake();
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
            brake();
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
            brake();
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
            brake();
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
    public void resetOrientation() {
        direction = "IDLE";
        switch (playerNumber) {
            case 1 -> setIcon(TankSprites.player1SpriteUp);
            case 2 -> setIcon(TankSprites.player2SpriteUp);
        }
    }

    public void revive() {
        if (!isAlive) {
            isAlive = true;
            initPositionUpdater();
        }
    }
}
