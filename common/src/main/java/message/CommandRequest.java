package message;

import commands.CommandArgs;
import commands.CommandData;
import lombok.Getter;
import lombok.Setter;

public class CommandRequest implements Request {
    @Getter
    private final CommandData commandData;
    @Getter
    private final CommandArgs commandArgs;
    @Getter
    @Setter
    private UserData userData;
    @Getter
    private ServerData serverData;

    public CommandRequest(CommandData commandData, CommandArgs commandArgs) {
        this.commandData = commandData;
        this.commandArgs = commandArgs;
    }

    public CommandRequest(CommandData commandData, CommandArgs commandArgs, UserData userData) {
        this(commandData, commandArgs);
        this.userData = userData;
    }

    public CommandRequest(CommandData commandData, CommandArgs commandArgs, ServerData serverData) {
        this(commandData, commandArgs);
        this.serverData = serverData;
    }
}
