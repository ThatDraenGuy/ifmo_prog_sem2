package message;

import collection.classes.MainCollectible;
import commands.CommandData;
import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;

public class ServerData implements Serializable {
    @Getter
    private final HashMap<String, CommandData> serverCommands;
    @Getter
    private final Class<? extends MainCollectible<?>> targetClass;

    public ServerData(HashMap<String, CommandData> serverCommands, Class<? extends MainCollectible<?>> targetClass) {
        this.serverCommands = serverCommands;
        this.targetClass = targetClass;
    }
}
