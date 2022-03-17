package message;

import commands.CommandArgs;
import commands.CommandData;
import lombok.Getter;

public class CommandRequest implements Request {
    @Getter
    private CommandData commandData;
    @Getter
    private CommandArgs commandArgs;

    public CommandRequest(CommandData cmdData, CommandArgs cmdArgs) {
        this.commandData = cmdData;
        this.commandArgs = cmdArgs;
    }

    @Override
    public Class<CommandResponse> getResponseType() {
        return CommandResponse.class;
    }
}
