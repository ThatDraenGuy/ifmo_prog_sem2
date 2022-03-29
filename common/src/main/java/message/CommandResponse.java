package message;

import commands.ActionResult;
import lombok.Getter;

public class CommandResponse implements Response {
    @Getter
    private final ActionResult actionResult;
    @Getter
    private ServerData serverData;

    public CommandResponse(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public CommandResponse(ActionResult actionResult, ServerData serverData) {
        this.actionResult = actionResult;
        this.serverData = serverData;
    }
}
