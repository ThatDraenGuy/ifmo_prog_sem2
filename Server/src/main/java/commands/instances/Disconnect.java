package commands.instances;

import commands.*;

public class Disconnect extends AbstractComplicatedCommand {

    public Disconnect() {
        super("disconnect", "command to disconnect from the server", CommandArgsType.NO_ARGS, CommandAccessLevel.GUEST);
    }

    @Override
    protected ActionResult complicatedAction(ExecutionPayload executionPayload) {
        if (executionPayload instanceof ServerExecutionPayload serverExecutionPayload) {
            serverExecutionPayload.getUserHandler().sendDisconnectRequest();
            return new ActionResult(true, "Successfully disconnected user");
        } else return new ActionResult(false, "Couldn't disconnect user");
    }
}
