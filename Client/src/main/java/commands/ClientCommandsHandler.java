package commands;

public class ClientCommandsHandler extends CommandsHandler {
    public CommandData getDisconnectCommandData() {
        return this.getCommandsData().get("disconnect");
    }

    public CommandData getApplyCollectionChangeCommandData() {
        return this.getCommandsData().get("apply_collection_change");
    }

    public CommandData getApplyFullCollectionCommandData() {
        return this.getCommandsData().get("apply_full_collection");
    }
}
