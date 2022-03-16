package message;

import commands.Command;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

public class ServerData implements Serializable {
    @Getter
    private final HashMap<String, Command> serverCommands;
    @Getter
    private final Class<?> targetClass;

    public ServerData(HashMap<String, Command> serverCommands, Class<?> targetClass) {
        this.serverCommands = serverCommands;
        this.targetClass = targetClass;
    }
}
