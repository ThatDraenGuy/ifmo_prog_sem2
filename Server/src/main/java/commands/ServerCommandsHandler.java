package commands;

import collection.CollectionHandler;
import message.CommandResponse;
import message.Request;
import message.Response;
import exceptions.CmdArgsAmountException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

/**
 * A class needed to handle all the commands. Stores all commands and provides access to them. Stores command history.
 * Accepts ... after performing command's action.
 * Also works as a link between commands and {@link collection.CollectionHandler};
 */
public class ServerCommandsHandler {
    private final HashMap<String, Command> commands;
    private final LinkedList<Command> commandHistory;
    private final CollectionHandler collectionHandler;

    public ServerCommandsHandler(CollectionHandler collectionHandler) {
        this.commands = new HashMap<>();
        this.collectionHandler = collectionHandler;
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

    public CollectionHandler getCollectionHandler() {
        return collectionHandler;
    }

    /**
     * @param request Accepts a request from
     * @return a response containing result of command's action.
     * @throws CmdArgsAmountException if arguments form a request don't match {@link CommandType} of the command
     */
    public Response executeCommand(Request request) throws CmdArgsAmountException {
        Command cmd = request.getCommand();
        CommandArgs cmdArgs = request.getCommandArgs();
        if (cmd.getCommandType() == CommandType.NO_ARGS && !cmdArgs.getArgs().equals("")) {
            throw new CmdArgsAmountException("Command \"" + cmd.getName() + "\" does not need arguments!");
        } else if (cmd.getCommandType() == CommandType.SIMPLE_ARG && cmdArgs.getArgs().equals("")) {
            throw new CmdArgsAmountException(cmd.getName() + " needs an argument!");
        }
        ActionResult result = cmd.action(cmdArgs);
        commandHistory.addLast(cmd);
        commandHistory.removeFirst();
        return new CommandResponse(result);
    }
}
