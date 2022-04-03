package commands.instances;

import commands.*;
import server.ServerHandler;

public class StopServer extends AbstractCommand {
    private final ServerHandler serverHandler;

    public StopServer(ServerHandler serverHandler) {
        super("stop_server", "command for stopping the server", CommandArgsType.NO_ARGS, CommandAccessLevel.DEV);
        this.serverHandler = serverHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        serverHandler.stopServer();
        return new ActionResult(true, "Stopping the server...");
    }
}
