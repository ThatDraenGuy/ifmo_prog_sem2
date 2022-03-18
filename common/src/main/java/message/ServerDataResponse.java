package message;

import commands.CommandData;
import lombok.Getter;

import java.util.HashMap;

public class ServerDataResponse implements Response {
    @Getter
    private final HashMap<String, CommandData> serverCommands;
    @Getter
    private final Class<?> targetClass;

    public ServerDataResponse(HashMap<String, CommandData> serverCommands, Class<?> targetClass) {
        this.serverCommands = serverCommands;
        this.targetClass = targetClass;
    }
}
