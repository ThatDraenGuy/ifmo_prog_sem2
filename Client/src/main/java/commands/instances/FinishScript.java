package commands.instances;

import commands.*;

public class FinishScript extends AbstractCommand {
    public FinishScript() {
        super("finish_script", "command needed for scripts", CommandArgsType.NO_ARGS, CommandAccessLevel.INTERNAL);
    }

    public ActionResult action(CommandArgs args) {
        return new ActionResult(true, "finishing script...", true);
    }
}
