package commands.instances;

import commands.*;

public class Disconnect extends AbstractCommand {

    public Disconnect() {
        super("disconnect", "command to disconnect from the server", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.GUEST);
    }

    @Override
    protected ActionResult action(ExecutionPayload executionPayload) {
        if (executionPayload instanceof ServerExecutionPayload serverExecutionPayload) {
            serverExecutionPayload.getUserHandler().sendDisconnectRequest();
            return new ActionResult(true, "disconnectSuccess");
        } else return new ActionResult(false, "disconnectFailure");
    }
}
