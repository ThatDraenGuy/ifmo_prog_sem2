package commands;

import exceptions.CommandNonExistentException;
import lombok.Getter;
import message.CommandRequest;
import message.CommandResponse;


import java.util.HashMap;
import java.util.LinkedList;

public class CommandsHandler {
    @Getter
    private final HashMap<String, Command> commands;
    private final LinkedList<Command> commandHistory;

    public CommandsHandler() {
        this.commands = new HashMap<>();
        this.commandHistory = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            commandHistory.add(null);
        }
    }

    public void addCommand(Command c) {
        final String name = c.getData().getName();
        if (!isInCommands(name)) {
            this.commands.put(name, c);
        }
    }

    public void addCommands(Command... cmds) {
        for (Command c : cmds) {
            addCommand(c);
        }
    }

    public boolean isInCommands(String name) {
        return this.commands.containsKey(name);
    }


    public LinkedList<Command> getCommandHistory() {
        return commandHistory;
    }

    public Command getCommand(CommandData data) throws CommandNonExistentException {
        String name = data.getName();
        if (commands.containsKey(name)) {
            return commands.get(name);
        }
        throw new CommandNonExistentException(name);
    }

    public HashMap<String, CommandData> getCommandsData() {
        HashMap<String, CommandData> res = new HashMap<>();
        for (String key : commands.keySet()) {
            res.put(key, commands.get(key).getData());
        }
        return res;
    }

    public CommandResponse executeCommand(CommandRequest request) {
        CommandData commandData = request.getCommandData();
        CommandArgs cmdArgs = request.getCommandArgs();
        try {
            Command command = getCommand(commandData);
            ActionResult result = command.action(cmdArgs);
            commandHistory.addLast(command);
            commandHistory.removeFirst();
            return new CommandResponse(result);
        } catch (CommandNonExistentException e) {
            return new CommandResponse(new ActionResult(false, e.getMessage()));
        }

    }
}
