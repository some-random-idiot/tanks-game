package game;

import tanks.TankShellPool;

import java.util.Observable;
import java.util.Observer;

public class Director implements Observer {
    public static Director directorInstance;
    public static int frameRate = 1000 / 60;
    public World world;
    public boolean onGoing = false;
    public TankShellPool tankShellPool;

    public void start(String mode) {
        onGoing = true;
        world = new World(mode);
        tankShellPool = new TankShellPool();
    }

    public static Director getInstance() {
        if (directorInstance == null) {
            directorInstance = new Director();
        }
        return directorInstance;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
