package message;

import commands.CommandData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

public class ServerData implements Serializable {
    @Getter
    private final HashMap<String, CommandData> serverCommands;
    @Getter
    private final Class<?> targetClass;
    @Getter
    @Setter
    private boolean disconnectRequested = false;

    public ServerData(HashMap<String, CommandData> serverCommands, Class<?> targetClass) {
        this.serverCommands = serverCommands;
        this.targetClass = targetClass;
    }
}
