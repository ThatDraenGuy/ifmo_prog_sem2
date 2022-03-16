package commands;

import exceptions.CommandArgsAmountException;
import message.CommandResponse;
import message.Request;
import message.Response;

import java.util.HashMap;
import java.util.LinkedList;

public class CommandsHandler {
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
        final String name = c.getName();
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

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public LinkedList<Command> getCommandHistory() {
        return commandHistory;
    }

    public Response executeCommand(Request request) {
        Command cmd = request.getCommand();
        CommandArgs cmdArgs = request.getCommandArgs();
        ActionResult result = cmd.action(cmdArgs);
        commandHistory.addLast(cmd);
        commandHistory.removeFirst();
        return new CommandResponse(result);
    }
}
