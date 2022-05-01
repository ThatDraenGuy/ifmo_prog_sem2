package commands.instances;

import web.ConnectionHandler;
import commands.*;

public class Disconnect extends AbstractCommand {
    private final ConnectionHandler connectionHandler;
    private final ExecutionController executionController;

    public Disconnect(ConnectionHandler connectionHandler, ExecutionController executionController) {
        super("disconnect_internal", "disconnects user form the server", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.INTERNAL);
        this.connectionHandler = connectionHandler;
        this.executionController = executionController;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        connectionHandler.disconnect();
        executionController.setUserAccessLevel(CommandAccessLevel.DISCONNECTED);
        return new ActionResult(true, "You have been successfully disconnected");
    }
}
