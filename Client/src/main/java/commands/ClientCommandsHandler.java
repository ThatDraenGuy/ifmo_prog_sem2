package commands;

import commands.instances.ApplyCollectionChange;
import commands.instances.ApplyFullCollection;
import commands.instances.Disconnect;
import commands.instances.SetAccount;
import lombok.Getter;

public class ClientCommandsHandler extends CommandsHandler {
    @Getter
    private CommandData disconnectCommandData;
    @Getter
    private CommandData applyCollectionChangeCommandData;
    @Getter
    private CommandData applyFullCollectionCommandData;
    @Getter
    private CommandData setAccountCommandData;

    @Override
    public void addCommand(Command command) {
        CommandData data = command.getData();
        if (command instanceof Disconnect) disconnectCommandData = data;
        else if (command instanceof ApplyCollectionChange) applyCollectionChangeCommandData = data;
        else if (command instanceof ApplyFullCollection) applyFullCollectionCommandData = data;
        else if (command instanceof SetAccount) setAccountCommandData = data;
        super.addCommand(command);
    }
}
