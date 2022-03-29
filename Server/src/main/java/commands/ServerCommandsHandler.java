package commands;

import lombok.Setter;
import message.CommandResponse;
import message.Response;
import message.ServerData;

public class ServerCommandsHandler extends CommandsHandler {
    @Setter
    private ServerData serverData;

    public ServerCommandsHandler() {
        super();
    }

    public ServerCommandsHandler(ServerData serverData) {
        super();
        this.serverData = serverData;
    }

    @Override
    protected Response createResponse(ActionResult actionResult) {
        return new CommandResponse(actionResult, serverData);
    }
}
