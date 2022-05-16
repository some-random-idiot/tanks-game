package tanks.commands;

import tanks.PlayerTank;

public class MoveLeftCommand implements GenericCommand {
    PlayerTank receiver;

    public MoveLeftCommand(PlayerTank player) {
        receiver = player;
    }

    @Override
    public void execute() {
        receiver.moveLeft();
    }
}
