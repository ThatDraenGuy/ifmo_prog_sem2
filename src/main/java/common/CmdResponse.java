package common;

import cmd.ActionResult;

public class CmdResponse implements Response{
    private ActionResult actionResult;
    public CmdResponse(ActionResult actionResult) {
        this.actionResult=actionResult;
    }

    @Override
    public ActionResult getActionResult() {
        return actionResult;
    }
}
