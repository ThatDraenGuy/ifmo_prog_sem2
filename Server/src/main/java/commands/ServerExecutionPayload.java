package commands;

import lombok.Getter;
import web.UserHandler;

public class ServerExecutionPayload extends ExecutionPayload {
    @Getter
    private final UserHandler userHandler;

    public ServerExecutionPayload(ExecutionPayload executionPayload, UserHandler userHandler) {
        super(executionPayload);
        this.userHandler = userHandler;
    }
}
