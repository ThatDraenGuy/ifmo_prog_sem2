package message;

import commands.CommandData;
import commands.ExecutionPayload;
import lombok.Getter;
import lombok.Setter;

public class CommandRequest implements Request {
    @Getter
    private final CommandData commandData;
    @Getter
    private final ExecutionPayload executionPayload;
    @Getter
    @Setter
    private UserData userData;
    @Getter
    private ServerData serverData;

    public CommandRequest(CommandData commandData, ExecutionPayload executionPayload) {
        this.commandData = commandData;
        this.executionPayload = executionPayload;
    }

    public CommandRequest(CommandData commandData, ExecutionPayload executionPayload, UserData userData) {
        this(commandData, executionPayload);
        this.userData = userData;
    }

    public CommandRequest(CommandData commandData, ExecutionPayload executionPayload, ServerData serverData) {
        this(commandData, executionPayload);
        this.serverData = serverData;
    }
}
