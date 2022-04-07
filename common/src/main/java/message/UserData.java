package message;

import commands.CommandData;
import lombok.Getter;

import java.io.Serializable;

public class UserData implements Serializable {
    @Getter
    private final CommandData disconnectCommandData;
    @Getter
    private final CommandData applyCollectionChangeCommandData;
    @Getter
    private final CommandData applyFullCollectionCommandData;
    @Getter
    private final long clientCollectionId;

    public UserData(long clientCollectionId, CommandData disconnectCommandData, CommandData applyCollectionChangeCommandData, CommandData applyFullCollectionCommandData) {
        this.disconnectCommandData = disconnectCommandData;
        this.applyCollectionChangeCommandData = applyCollectionChangeCommandData;
        this.applyFullCollectionCommandData = applyFullCollectionCommandData;
        this.clientCollectionId = clientCollectionId;
    }
}
