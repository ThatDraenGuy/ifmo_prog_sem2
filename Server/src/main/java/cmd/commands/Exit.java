package cmd.commands;

import cmd.AbstractCommand;
import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;

/**
 * A command to exit the program
 */
public class Exit extends AbstractCommand {
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)", CmdType.NO_ARGS);
    }

    public ActionResult action(CmdArgs args) {
        return new ActionResult(true, "exit");
    }
}
