package game;

import java.util.Observable;
import java.util.Observer;

public class Director implements Observer {
    public static int frameRate = 1000 / 60;
    public World world;

    public void start(String mode) {
        world = new World(mode);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
