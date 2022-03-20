package commands.instances;

import commands.AbstractCommand;
import commands.ActionResult;
import commands.CommandArgs;
import commands.CommandType;

public class FinishScript extends AbstractCommand {
    public FinishScript() {
        super("finish_script", "command needed for scripts", CommandType.NO_ARGS);
    }

    public ActionResult action(CommandArgs args) {
        return new ActionResult(true, "finish_script", true);
    }
}
