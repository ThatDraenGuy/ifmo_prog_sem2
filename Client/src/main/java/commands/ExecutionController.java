package commands;

import commands.instances.Disconnect;
import threads.ThreadHandler;
import commands.instances.FetchServerData;
import console.ConsoleHandler;
import exceptions.CommandArgsAmountException;
import lombok.Getter;
import lombok.Setter;
import message.*;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

public class ExecutionController {
    @Getter
    private CommandAccessLevel userAccessLevel;
    @Setter
    private ThreadHandler threadHandler;
    final private ClientCommandsHandler clientCommandsHandler;
    final private ConsoleHandler consoleHandler;
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
    private UserData userData;

    public ExecutionController(ClientCommandsHandler clientCommandsHandler, ConsoleHandler consoleHandler) {
        this.clientCommandsHandler = clientCommandsHandler;
        this.consoleHandler = consoleHandler;
        this.accessibleClientCommands = new HashMap<>();
        this.accessibleServerCommands = new HashMap<>();
        this.history = new LinkedList<>();
        this.clientCommands = null;
        this.serverCommands = null;
        this.userAccessLevel = CommandAccessLevel.DISCONNECTED;
        this.userData = new UserData(clientCommandsHandler.getDisconnectCommandData());
    }

    public void initialize() {
        clientCommands = clientCommandsHandler.getCommandsData();
//        fetchServerData();
        historySetup();
        commandSetup();
    }

    private void commandSetup() {
        accessibleClientCommands.clear();
        accessibleServerCommands.clear();
        for (String key : clientCommands.keySet()) {
            CommandData data = clientCommands.get(key);
            if (data.getAccessLevel().compareTo(userAccessLevel) <= 0) accessibleClientCommands.put(key, data);
        }
        if (serverCommands != null) {
            for (String key : serverCommands.keySet()) {
                CommandData data = serverCommands.get(key);
                if (data.getAccessLevel().compareTo(userAccessLevel) <= 0) accessibleServerCommands.put(key, data);
            }
        }
    }

    private void historySetup() {
        final int historyLength = 5;
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
        handleServerData(request.getServerData());
//        request.setUserData(userData);
        history.addLast(request.getCommandData());
        history.removeFirst();
        Response response;
        consoleHandler.debugMessage("executing command...");
        if (isClientCommand(request)) response = clientCommandsHandler.executeCommand(request);
        else response = threadHandler.sendRequest(request);
        return handleResponse(response);
    }

    public Request createRequest(CommandData commandData, CommandArgs commandArgs) {
        userData = new UserData(clientCommandsHandler.getDisconnectCommandData());
        return new CommandRequest(commandData, commandArgs, userData);
    }

//    private Response executeCommand(Command command) {
//        //TODO
//        Request request = createRequest(command.getData(), new CommandArgs(""));
//        try {
//            return executeCommand(request);
//        } catch (CommandArgsAmountException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private Response handleResponse(Response response) {
        handleServerData(response.getServerData());
        return response;
    }

    private void handleServerData(ServerData serverData) {
        if (serverData != null) {
            targetClass = serverData.getTargetClass();
            serverCommands = serverData.getServerCommands();
            commandSetup();
//            if (serverData.isDisconnectRequested()) forceDisconnect();
        }
    }

    public void setUserAccessLevel(CommandAccessLevel accessLevel) {
        this.userAccessLevel = accessLevel;
        commandSetup();
    }

    public void exit() {
        threadHandler.stop();
    }
}
