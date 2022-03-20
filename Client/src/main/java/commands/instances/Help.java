package commands.instances;

import commands.*;

import java.util.HashMap;

/**
 * A Help command. Displays all commands and their descriptions.
 */
//TODO rework, replace
public class Help extends AbstractCommand {
    private String message = null;
    private final CommandsHandler cmdHandler;
    private final HashMap<String, CommandData> serverCommands;

    public Help(CommandsHandler cmdHandler, HashMap<String, CommandData> serverCommands) {
        super("help", "вывести справку по доступным командам", CommandType.NO_ARGS);
        this.cmdHandler = cmdHandler;
        this.serverCommands = serverCommands;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        if (message == null) {
            message = createMessage();
        }
        return new ActionResult(true, message);
    }

    private String createMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("Local commands:\n");
        for (CommandData data : cmdHandler.getCommandsData().values()) {
            builder.append(data.getName()).append(": ").append(data.getDescription()).append("\n");
        }
        builder.append("Server commands:\n");
        for (CommandData data : serverCommands.values()) {
            builder.append(data.getName()).append(": ").append(data.getDescription()).append("\n");
        }
        message = builder.toString();
        return message;
    }
}
