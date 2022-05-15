package tanks;

import game.Director;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TankShellPool {
    private List<TankShell> shellPool = new ArrayList<>();
    private Thread shellPoolManager;
    private LocalTime startTime = null;

    public TankShellPool() {
        int size = 30;
        for(int i = 0; i < size; i ++) {
            shellPool.add(new TankShell(-999, -999));
        }

        shellPoolManager = new Thread() {
            @Override
            public void run() {
                while (Director.getInstance().onGoing) {
                    if (shellPool.size() > 30 && startTime == null) {
                        startTime = LocalTime.now().withNano(0);
                    }
                    if (LocalTime.now().withNano(0).minusSeconds(30).equals(startTime)) {
                        shellPool = shellPool.subList(0, 30);
                        startTime = null;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        shellPoolManager.start();
    }

    public TankShell requestShell(int x, int y) {
        TankShell shell;
        if (shellPool.isEmpty()) {
            shellPool.add(new TankShell(-999, -999));;
        }
        shell = shellPool.remove(0);
        shell.setBounds(x, y, shell.getWidth(), shell.getHeight());
        return shell;
    }

    public void releaseShell(TankShell shell) {
        shellPool.add(shell);
    }
}
