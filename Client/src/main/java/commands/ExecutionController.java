package commands;


import collection.CollectionClassesHandler;
import commands.instances.FetchServerData;
import threads.ThreadHandler;
import console.ConsoleHandler;
import exceptions.CommandArgsAmountException;
import lombok.Getter;
import lombok.Setter;
import message.*;
import utility.LimitedCollection;

import java.util.HashMap;

public class ExecutionController {
    @Getter
    private CommandAccessLevel userAccessLevel;
    @Setter
    private ThreadHandler threadHandler;
    final private ClientCommandsHandler clientCommandsHandler;
    final private ConsoleHandler consoleHandler;
    @Getter
    private final CollectionClassesHandler targetClassHandler;
    private HashMap<String, CommandData> clientCommands;
    private HashMap<String, CommandData> serverCommands;
    @Getter
    private final HashMap<String, CommandData> accessibleClientCommands;
    @Getter
    private final HashMap<String, CommandData> accessibleServerCommands;
    @Getter
    private final LimitedCollection<CommandData> history;
    private UserData userData;

    public ExecutionController(ClientCommandsHandler clientCommandsHandler, ConsoleHandler consoleHandler, CollectionClassesHandler targetClassHandler) {
        this.clientCommandsHandler = clientCommandsHandler;
        this.consoleHandler = consoleHandler;
        this.accessibleClientCommands = new HashMap<>();
        this.accessibleServerCommands = new HashMap<>();
        this.history = new LimitedCollection<>(5);
        this.clientCommands = null;
        this.serverCommands = null;
        this.userAccessLevel = CommandAccessLevel.DISCONNECTED;
        this.targetClassHandler = targetClassHandler;
        this.userData = createUserData();
    }

    public void initialize() {
        clientCommands = clientCommandsHandler.getCommandsData();
//        fetchServerData();
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
        request = handleRequest(request);
        history.add(request.getCommandData());
        Response response;
        consoleHandler.debugMessage("executing command...");
        if (isClientCommand(request)) response = clientCommandsHandler.executeCommand(request);
        else response = threadHandler.sendRequest(request);
        return handleResponse(response);
    }

    public Request createRequest(CommandData commandData, CommandArgs commandArgs) {
        userData = createUserData();
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
    private Request handleRequest(Request request) {
        handleServerData(request.getServerData());
        request.setUserData(userData);
        return request;
    }

    private Response handleResponse(Response response) {
        handleServerData(response.getServerData());
        return response;
    }

    private void handleServerData(ServerData serverData) {
        if (serverData != null) {
            targetClassHandler.handleTargetClass(serverData.getTargetClass());
            serverCommands = serverData.getServerCommands();
            commandSetup();
//            if (serverData.isDisconnectRequested()) forceDisconnect();
        }
    }

    public void setUserAccessLevel(CommandAccessLevel accessLevel) {
        this.userAccessLevel = accessLevel;
        commandSetup();
    }

    private UserData createUserData() {
        return new UserData(targetClassHandler.getCurrentId(), clientCommandsHandler.getDisconnectCommandData(), clientCommandsHandler.getApplyCollectionChangeCommandData(), clientCommandsHandler.getApplyFullCollectionCommandData());
    }
//    public void fetchServerData() {
//        executeCommand(new FetchServerData());
//    }
}
