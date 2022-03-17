package commands.instances;

import commands.*;

/**
 * A Help command. Displays all commands and their descriptions.
 */
//TODO rework, replace
public class Help extends AbstractCommand {
    private String message = "";
    ServerCommandsHandler cmdHandler;

    public Help(ServerCommandsHandler cmdHandler) {
        super("help", "вывести справку по доступным командам", CommandType.NO_ARGS);
        this.cmdHandler = cmdHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        if (message.equals("")) {
            message = createMessage();
        }
        return new ActionResult(true, message);
    }

    private String createMessage() {
        for (Command cmd : cmdHandler.getCommands().values()) {
            message += cmd.getData().getName() + ": " + cmd.getData().getDescription() + "\n";
        }
        return message;
    }
}
