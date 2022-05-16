package tanks.commands;

import tanks.PlayerTank;

public class MoveDownCommand implements GenericCommand {
    PlayerTank receiver;

    public MoveDownCommand(PlayerTank player) {
        receiver = player;
    }

    @Override
    public void execute() {
        receiver.moveDown();
    }
}
