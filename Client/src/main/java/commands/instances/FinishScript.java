package commands.instances;

import commands.*;
import threads.ConsoleInteractionController;

public class FinishScript extends AbstractCommand {
    private final ConsoleInteractionController clientInteractionController;

    public FinishScript(ConsoleInteractionController consoleInteractionController) {
        super("finish_script", "command needed for scripts", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.INTERNAL);
        this.clientInteractionController = consoleInteractionController;
    }

    public ActionResult action(ExecutionPayload executionPayload) {
        clientInteractionController.setExitQueried(true);
        return new ActionResult(true, "finishing script...");
    }
}
