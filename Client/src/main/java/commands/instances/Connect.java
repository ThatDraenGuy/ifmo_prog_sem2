package commands.instances;

import web.ConnectionHandler;
import threads.ThreadHandler;
import commands.*;

public class Connect extends AbstractCommand {
    private final ConnectionHandler connectionHandler;
    private final ThreadHandler threadHandler;
    private final ExecutionController executionController;

    public Connect(ConnectionHandler connectionHandler, ExecutionController executionController, ThreadHandler threadHandler) {
        super("connect", "подключиться к серверу", CommandArgsType.NO_ARGS, CommandAccessLevel.DISCONNECTED);
        this.connectionHandler = connectionHandler;
        this.executionController = executionController;
        this.threadHandler = threadHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        connectionHandler.connect();
        threadHandler.reloadMessageReader();
        executionController.setUserAccessLevel(CommandAccessLevel.DEV);
        //TODO normal level changing
        return new ActionResult(true, "You have been successfully connected");
    }
}
