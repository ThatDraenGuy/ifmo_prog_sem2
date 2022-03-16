package message;

import commands.ActionResult;

public class CommandResponse implements Response {
    private ActionResult actionResult;

    public CommandResponse(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    @Override
    public ActionResult getActionResult() {
        return actionResult;
    }
}
