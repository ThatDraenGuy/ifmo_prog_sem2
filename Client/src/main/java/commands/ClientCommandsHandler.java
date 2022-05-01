package commands;

public class ClientCommandsHandler extends CommandsHandler {
    public CommandData getDisconnectCommandData() {
        return this.getCommandsData().get("disconnect_internal");
    }

    public CommandData getApplyCollectionChangeCommandData() {
        return this.getCommandsData().get("apply_collection_change");
    }

    public CommandData getApplyFullCollectionCommandData() {
        return this.getCommandsData().get("apply_full_collection");
    }

    public CommandData getSetAccountCommandData() {
        return this.getCommandsData().get("set_account");
    }
}
