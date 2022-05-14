package tanks.commands;

import tanks.PlayerTank;

public class MoveRightCommand implements GenericCommand {
    PlayerTank receiver;

    public MoveRightCommand(PlayerTank player) {
        receiver = player;
    }

    @Override
    public void execute() {
        receiver.moveRight();
    }
}
