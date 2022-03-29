package commands.instances;

import client.App;
import commands.*;

import java.util.HashMap;

/**
 * A Help command. Displays all commands and their descriptions.
 */
//TODO rework, replace
public class Help extends AbstractCommand {
    private final CommandsExecutor commandsExecutor;

    public Help(CommandsExecutor commandsExecutor) {
        super("help", "вывести справку по доступным командам", CommandArgsType.NO_ARGS);
        this.commandsExecutor = commandsExecutor;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        String message = createMessage();
        return new ActionResult(true, message);
    }

    private String createMessage() {
        HashMap<String, CommandData> clientCommands = commandsExecutor.getAccessibleClientCommands();
        HashMap<String, CommandData> serverCommands = commandsExecutor.getAccessibleServerCommands();
        StringBuilder builder = new StringBuilder();
        builder.append("Local commands:\n");
        for (CommandData data : clientCommands.values()) {
            builder.append(data.getName()).append(": ").append(data.getDescription()).append("\n");
        }
        builder.append("Server commands:\n");
        for (CommandData data : serverCommands.values()) {
            builder.append(data.getName()).append(": ").append(data.getDescription()).append("\n");
        }
        return builder.toString();
    }
}
