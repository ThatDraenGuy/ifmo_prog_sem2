package commands;

import client.ConnectionHandler;
import commands.instances.Disconnect;
import commands.instances.FetchServerData;
import exceptions.CommandArgsAmountException;
import lombok.Getter;
import message.CommandRequest;
import message.Request;
import message.Response;
import message.ServerData;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

public class CommandsExecutor {
    final private static Command fetchServerDataCommand = new FetchServerData();
    final private static Command disconnectCommand = new Disconnect();
    @Getter
    private CommandAccessLevel userAccessLevel = CommandAccessLevel.DEV;
    final private ConnectionHandler connectionHandler;
    final private CommandsHandler clientCommandsHandler;
    private HashMap<String, CommandData> clientCommands;
    private HashMap<String, CommandData> serverCommands;
    @Getter
    private Class<?> targetClass;
    @Getter
    private final HashMap<String, CommandData> accessibleClientCommands;
    @Getter
    private final HashMap<String, CommandData> accessibleServerCommands;
    @Getter
    private final Deque<CommandData> history;
    private final int historyLength = 5;

    public CommandsExecutor(ConnectionHandler connectionHandler, CommandsHandler clientCommandsHandler) {
        this.connectionHandler = connectionHandler;
        this.clientCommandsHandler = clientCommandsHandler;
        this.accessibleClientCommands = new HashMap<>();
        this.accessibleServerCommands = new HashMap<>();
        this.history = new LinkedList<>();
    }

    public void start() {
        clientCommands = clientCommandsHandler.getCommandsData();
        fetchServerData();
        historySetup();
        commandAccessSetup();
    }

    private void commandAccessSetup() {
        accessibleClientCommands.clear();
        accessibleServerCommands.clear();
        for (String key : clientCommands.keySet()) {
            CommandData data = clientCommands.get(key);
            if (data.getAccessLevel().compareTo(userAccessLevel) <= 0) accessibleClientCommands.put(key, data);
        }
        for (String key : serverCommands.keySet()) {
            CommandData data = serverCommands.get(key);
            if (data.getAccessLevel().compareTo(userAccessLevel) <= 0) accessibleServerCommands.put(key, data);
        }
    }

    private void historySetup() {
        for (int i = 0; i <= historyLength; i++) {
            history.add(null);
        }
    }

    public boolean isInCommands(String name) {
        return (accessibleServerCommands.containsKey(name) || accessibleClientCommands.containsKey(name));
        //TODO wtf?
    }

    public boolean isClientCommand(String name) {
        return accessibleClientCommands.containsKey(name);
    }

    public boolean isClientCommand(Request request) {
        return isClientCommand(request.getCommandData().getName());
        //TODO rework
    }

    public Response executeCommand(Request request) throws CommandArgsAmountException {
        history.addLast(request.getCommandData());
        history.removeFirst();
        if (isClientCommand(request)) {
            return clientCommandsHandler.executeCommand(request);
        }
        return connectionHandler.send(request);
    }

    private Response executeCommand(Command command) {
        Request request = new CommandRequest(command.getData(), new CommandArgs(""));
        try {
            return executeCommand(request);
        } catch (CommandArgsAmountException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleServerData(ServerData serverData) {
        if (serverData != null) {
            targetClass = serverData.getTargetClass();
            serverCommands = serverData.getServerCommands();
        }
    }

    public void fetchServerData() {
        Response response = executeCommand(fetchServerDataCommand);
        handleServerData(response.getServerData());
    }

    public void disconnect() {
        executeCommand(disconnectCommand);
    }

    public void setUserAccessLevel(CommandAccessLevel accessLevel) {
        this.userAccessLevel = accessLevel;
        commandAccessSetup();
    }
}
