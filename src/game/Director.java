package game;

import tanks.TankShellPool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class Director implements Observer {
    public static Director directorInstance;
    public static int frameRate = 1000 / 60;
    private String currentMode;
    public World world;
    public int score;
    public int wave;
    public volatile boolean onGoing = false;
    public TankShellPool tankShellPool;

    public void start(String mode) {
        currentMode = mode;

        world = new World(mode);
        world.addObserver(this);
        tankShellPool = new TankShellPool();

        onGoing = true;
    }

    public static Director getInstance() {
        if (directorInstance == null) {
            directorInstance = new Director();
        }
        return directorInstance;
    }

    @Override
    public void update(Observable o, Object arg) {
        Thread gameOver = new Thread() {
            @Override
            public void run() {
                onGoing = false;

                JDialog dialog = new JDialog();
                dialog.setTitle("Game Over");
                dialog.setSize(300, 200);

                JPanel container = new JPanel();
                container.setLayout(new BorderLayout());

                JLabel scoreLabel = new JLabel("Score: " + score);
                scoreLabel.setFont(new Font("Courier New", Font.BOLD, 20));

                JLabel waveLabel = new JLabel("Waves passed: " + wave);
                waveLabel.setFont(new Font("Courier New", Font.BOLD, 20));

                JButton restartButton = new JButton("Restart");
                restartButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onGoing = true;
                        dialog.dispose();
                    }
                });

                container.add(scoreLabel, BorderLayout.NORTH);
                container.add(waveLabel, BorderLayout.CENTER);
                container.add(restartButton, BorderLayout.SOUTH);
                container.setVisible(true);

                dialog.add(container);
                dialog.setVisible(true);

                while (true) {
                    if (onGoing) {
                        world.reset();
                        break;
                    }
                }
            }
        };
        gameOver.start();
    }
}
