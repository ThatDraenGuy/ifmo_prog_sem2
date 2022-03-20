package commands.instances;

import client.ConnectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import message.DisconnectRequest;

/**
 * A command to exit the program
 */
public class Exit extends AbstractCommand {
    private final ConnectionHandler connectionHandler;

    public Exit(ConnectionHandler connectionHandler) {
        super("exit", "завершить программу (без сохранения в файл)", CommandType.NO_ARGS);
        this.connectionHandler = connectionHandler;
    }

    public ActionResult action(CommandArgs args) {
        connectionHandler.send(new DisconnectRequest());
        return new ActionResult(true, "exit", true);
    }
}
