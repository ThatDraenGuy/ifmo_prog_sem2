package commands;

public class ClientCommandsHandler extends CommandsHandler {
    public CommandData getDisconnectCommandData() {
        return this.getCommandsData().get("disconnect");
    }
}
