package cmd.commands;

import cmd.*;

import java.util.LinkedList;

/**
 * A History command. Displays 5 last used commands.
 */
public class History extends AbstractCommand {
    private CmdHandler cmdHandler;

    public History(CmdHandler cmdHandler) {
        super("history", "вывести последние 5 команд", CmdType.NO_ARGS);
        this.cmdHandler=cmdHandler;
    }

    @Override
    public ActionResult action(CmdArgs args) {
        LinkedList<Command> cmds = cmdHandler.getCmdHistory();
        StringBuilder str = new StringBuilder();
        for (Command cmd : cmds) {
            if (cmd != null) {
                str.append(cmd.getName()).append("\n");
            }
        }
        return new ActionResult(true, str.toString());
    }
}
