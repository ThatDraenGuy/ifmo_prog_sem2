package commands.instances;

import commands.*;
import threads.ClientInteractionController;

public class FinishScript extends AbstractCommand {
    private final ClientInteractionController clientInteractionController;

    public FinishScript(ClientInteractionController clientInteractionController) {
        super("finish_script", "command needed for scripts", new CommandArgsInfo(CommandArgsType.NO_ARGS), CommandAccessLevel.INTERNAL);
        this.clientInteractionController = clientInteractionController;
    }

    public ActionResult action(ExecutionPayload executionPayload) {
        clientInteractionController.setExitQueried(true);
        return new ActionResult(true, "finishing script...");
    }
}
