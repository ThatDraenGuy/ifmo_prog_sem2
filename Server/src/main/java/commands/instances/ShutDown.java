package commands.instances;

import commands.*;
import server.ServerHandler;

public class ShutDown extends AbstractCommand {
    private final ServerHandler serverHandler;

    public ShutDown(ServerHandler serverHandler) {
        super("shutdown", "command for stopping the server", CommandArgsType.NO_ARGS, CommandAccessLevel.DEV, true);
        this.serverHandler = serverHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        serverHandler.shutdown();
        return new ActionResult(true, "Shutting down the server...", true);
    }
}
