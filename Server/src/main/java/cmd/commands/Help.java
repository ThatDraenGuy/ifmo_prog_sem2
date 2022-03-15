package cmd.commands;

import cmd.*;

/**
 * A Help command. Displays all commands and their descriptions.
 */
public class Help extends AbstractCommand {
    private String message = "";
    private CmdHandler cmdHandler;

    public Help(CmdHandler cmdHandler) {
        super("help", "вывести справку по доступным командам", CmdType.NO_ARGS);
        this.cmdHandler = cmdHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        if (message.equals("")) {
            message = createMessage();
        }
        return new ActionResult(true, message);
    }

    private String createMessage() {
        for (Command cmd : cmdHandler.getCmds().values()) {
            message += cmd.getName() + ": " + cmd.getDescription() + "\n";
        }
        return message;
    }
}
