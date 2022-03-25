package commands.instances;

import client.ConnectionHandler;
import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;
import console.ConsoleHandler;
import message.DisconnectRequest;

/**
 * A command to exit the program
 */
public class Exit extends AbstractCommand {
    private final ConnectionHandler connectionHandler;
    private final ConsoleHandler consoleHandler;

    public Exit(ConnectionHandler connectionHandler, ConsoleHandler consoleHandler) {
        super("exit", "завершить программу (без сохранения в файл)", CommandType.NO_ARGS);
        this.connectionHandler = connectionHandler;
        this.consoleHandler = consoleHandler;
    }

    public ActionResult action(CommandArgs args) {
        if (consoleHandler.promptAgreement("Are you sure you want to exit?")) {
            connectionHandler.send(new DisconnectRequest());
            return new ActionResult(true, "exiting...", true);
        } else return new ActionResult(false, "Exit cancelled");
    }
}
