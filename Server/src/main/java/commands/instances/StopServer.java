package commands.instances;

import commands.*;
import web.ServerHandler;

public class StopServer extends AbstractCommand {
    private final ServerHandler serverHandler;

    public StopServer(ServerHandler serverHandler) {
        super("stop_server", "command for stopping the server", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.DEV);
        this.serverHandler = serverHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        serverHandler.stopServer();
        return new ActionResult(true, "Stopping the server...");
    }
}
