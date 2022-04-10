package commands;

import commands.instances.FetchServerData;
import lombok.Getter;
import lombok.Setter;
import message.CommandResponse;
import message.Response;
import message.ServerData;

public class ServerCommandsHandler extends CommandsHandler {
    @Getter
    @Setter
    private ServerData serverData;
    @Getter
    private final Command fetchServerDataCommand = new FetchServerData();


    public ServerCommandsHandler() {
        super();
    }


    @Override
    protected Response createResponse(ActionResult actionResult) {
        return new CommandResponse(actionResult, serverData);
    }

    public CommandData getSaveCommandData() {
        return getCommandsData().get("save");
    }
}
