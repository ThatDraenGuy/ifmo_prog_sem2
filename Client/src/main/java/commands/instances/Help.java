package commands.instances;

import commands.*;

/**
 * A Help command. Displays all commands and their descriptions.
 */
//TODO rework, replace
public class Help extends AbstractCommand {
    private String message = null;
    CommandsHandler cmdHandler;

    public Help(CommandsHandler cmdHandler) {
        super("help", "вывести справку по доступным командам", CommandType.NO_ARGS);
        this.cmdHandler = cmdHandler;
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
        for (CommandData data : cmdHandler.getCommandsData().values()) {
            builder.append(data.getName()).append(": ").append(data.getDescription()).append("\n");
        }
        message = builder.toString();
        return message;
    }
}
