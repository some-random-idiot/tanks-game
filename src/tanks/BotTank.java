package tanks;

import java.util.Random;

public class BotTank extends GenericTank {


    public BotTank(int x, int y) {
        super();

        setIcon(TankSprites.botSpriteUp);
        setBounds(x, y, 60, 60);
        initPositionUpdater();
        initAI();
    }

    private void initAI() {
        Thread movement = new Thread() {
            @Override
            public void run() {
                while (isAlive) {
                    Random random = new Random();
                    switch (random.nextInt((5 - 1) + 1) + 1) {
                        case 1 -> moveUp();
                        case 2 -> moveDown();
                        case 3 -> moveLeft();
                        case 4 -> moveRight();
                        case 5 -> brake();
                    }

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        movement.start();
    }

    @Override
    public void moveUp() {
        if (isAlive) {
            setIcon(TankSprites.botSpriteUp);
            direction = "UP";
        }
    }

    @Override
    public void moveDown() {
        if (isAlive) {
            setIcon(TankSprites.botSpriteDown);
            direction = "DOWN";
        }
    }

    @Override
    public void moveLeft() {
        if (isAlive) {
            setIcon(TankSprites.botSpriteLeft);
            direction = "LEFT";
        }
    }

    @Override
    public void moveRight() {
        if (isAlive) {
            setIcon(TankSprites.botSpriteRight);
            direction = "RIGHT";
        }
    }
}
