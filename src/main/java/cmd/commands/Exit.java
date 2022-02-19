package cmd.commands;

import cmd.ActionResult;
import cmd.CmdArgs;
import cmd.CmdType;
import cmd.AbstractCommand;

public class Exit extends AbstractCommand {
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)", CmdType.NO_ARGS);
    }
    public ActionResult action(CmdArgs args) {
        System.exit(0);
        return new ActionResult(true, "exiting...");
        //TODO implement a "are you sure" feature (ConsoleHandler?)
    }
}
