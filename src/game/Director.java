package game;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Director implements Observer {
    private static String gameState = "N/A";
    private World world;

    public void start() {
        gameState = "ONGOING";
        world = new World();
        world.initWorld();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
