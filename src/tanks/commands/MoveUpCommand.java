package tanks.commands;

import tanks.PlayerTank;

public class MoveUpCommand implements GenericCommand {
    PlayerTank receiver;

    public MoveUpCommand(PlayerTank player) {
        receiver = player;
    }

    @Override
    public void execute() {
        receiver.moveUp();
    }
}
