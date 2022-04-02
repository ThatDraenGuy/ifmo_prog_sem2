package message;

import commands.CommandData;
import lombok.Getter;

import java.io.Serializable;

public class UserData implements Serializable {
    @Getter
    private final CommandData disconnectCommandData;

    public UserData(CommandData disconnectCommandData) {
        this.disconnectCommandData = disconnectCommandData;
    }
}
