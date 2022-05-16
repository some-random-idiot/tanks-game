package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class Director implements Observer {
    private static Director directorInstance;
    public static final int frameRate = 1000 / 60;
    private String currentMode;
    public World world;
    public int score;
    public int wave = 1;
    public volatile boolean onGoing = false;

    public void start(String mode) {
        currentMode = mode;
        onGoing = true;
        world = new World(mode);
    }

    public static Director getInstance() {
        if (directorInstance == null) {
            directorInstance = new Director();
        }
        return directorInstance;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == "GAME_OVER") {
            Thread gameOver = new Thread() {
                @Override
                public void run() {
                    onGoing = false;

                    JDialog dialog = new JDialog();
                    dialog.setTitle("Game Over");
                    dialog.setSize(300, 200);
                    dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

                    JPanel container = new JPanel();
                    container.setLayout(new BorderLayout());

                    JLabel scoreLabel = new JLabel("Score: " + score);
                    scoreLabel.setFont(new Font("Courier New", Font.BOLD, 26));

                    JLabel waveLabel = new JLabel("Waves cleared: " + (wave - 1));
                    waveLabel.setFont(new Font("Courier New", Font.BOLD, 26));

                    JButton restartButton = new JButton("OK");
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

                    wave = 1;

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
        if (arg == "GAMESPACE_CLOSED") {
            onGoing = false;
        }
        if (arg == "BOT_TANK_DESTROYED") {
            score++;
        }
        if (arg == "WAVE_CLEARED") {
            onGoing = false;
            wave += 1;
            world.reset();
            onGoing = true;
        }
    }
}
