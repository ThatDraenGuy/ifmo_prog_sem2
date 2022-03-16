package commands.instances;

import commands.*;

import java.util.LinkedList;

/**
 * A History command. Displays 5 last used commands.
 */
public class History extends AbstractCommand {
    private ServerCommandsHandler cmdHandler;

    public History(ServerCommandsHandler cmdHandler) {
        super("history", "вывести последние 5 команд", CommandType.NO_ARGS);
        this.cmdHandler = cmdHandler;
    }

    @Override
    public ActionResult action(CommandArgs args) {
        LinkedList<Command> cmds = cmdHandler.getCommandHistory();
        StringBuilder str = new StringBuilder();
        for (Command cmd : cmds) {
            if (cmd != null) {
                str.append(cmd.getName()).append("\n");
            }
        }
        return new ActionResult(true, str.toString());
    }
}
