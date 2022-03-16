package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

/**
 * A command to exit the program
 */
public class Exit extends AbstractCommand {
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)", CommandType.NO_ARGS);
    }

    public ActionResult action(CommandArgs args) {
        return new ActionResult(true, "exit");
    }
}
