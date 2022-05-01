package commands.instances;

import commands.*;
import web.ServerHandler;

public class Shutdown extends AbstractCommand {
    private final ServerHandler serverHandler;

    public Shutdown(ServerHandler serverHandler) {
        super("shutdown", "server's emergency shutdown", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.DEV);
        this.serverHandler = serverHandler;
    }

    @Override
    public ActionResult action(ExecutionPayload executionPayload) {
        serverHandler.shutdown();
        return new ActionResult(true, "Stopping the server...");
    }
}
