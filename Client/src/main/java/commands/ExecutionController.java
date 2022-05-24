package commands;


import collection.CollectionClassesHandler;
import exceptions.CommandNonExistentException;
import exceptions.ConnectionException;
import security.CurrentAccount;
import threads.MessageSender;
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
    private final MessageSender messageSender;
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

    public ExecutionController(ClientCommandsHandler clientCommandsHandler, ConsoleHandler consoleHandler, CollectionClassesHandler targetClassHandler, MessageSender messageSender) {
        this.clientCommandsHandler = clientCommandsHandler;
        this.messageSender = messageSender;
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

    public CommandData getCommandData(String name) throws CommandNonExistentException {
        if (isAccessibleCommand(name)) {
            if (isClientCommand(name)) {
                return accessibleClientCommands.get(name);
            } else {
                return accessibleServerCommands.get(name);
            }
        } else {
            throw new CommandNonExistentException(name);
        }
    }

    public boolean isAccessibleCommand(String name) {
        return (accessibleServerCommands.containsKey(name) || accessibleClientCommands.containsKey(name));
    }

    public boolean isClientCommand(String name) {
        return clientCommands.containsKey(name);
    }

    public boolean isClientCommand(Request request) {
        return isClientCommand(request.getCommandData().getName());
    }

    public Response executeCommand(Request request) throws CommandArgsAmountException, ConnectionException {
        handleRequest(request);
        if (!request.getCommandData().getAccessLevel().equals(CommandAccessLevel.INTERNAL))
            history.add(request.getCommandData());
        Response response;
        consoleHandler.debugMessage("executing command: " + request.getCommandData().getName() + " " + request.getExecutionPayload().getAccount() + " " + CurrentAccount.getAccount());
        consoleHandler.debugMessage("is client command: " + isClientCommand(request));
        if (isClientCommand(request)) response = clientCommandsHandler.executeCommand(request);
        else response = messageSender.sendRequest(request);
        return handleResponse(response);
    }

    public Request createRequest(CommandData commandData, CommandArgs commandArgs) {
        userData = createUserData();
        return new CommandRequest(commandData, new ExecutionPayload(commandArgs), userData);
    }

    private void handleRequest(Request request) {
        handleServerData(request.getServerData());
        request.setUserData(userData);
    }

    private Response handleResponse(Response response) {
        handleServerData(response.getServerData());
        consoleHandler.debugMessage(response.getActionResult().getMessage());
        return response;
    }

    private void handleServerData(ServerData serverData) {
        if (serverData != null) {
            targetClassHandler.handleTargetClass(serverData.getTargetClass());
            serverCommands = serverData.getServerCommands();
            commandSetup();
        }
    }

    public void setUserAccessLevel(CommandAccessLevel accessLevel) {
        this.userAccessLevel = accessLevel;
        commandSetup();
    }

    private UserData createUserData() {
        return new UserData(targetClassHandler.getCurrentId(), clientCommandsHandler.getDisconnectCommandData(), clientCommandsHandler.getApplyCollectionChangeCommandData(),
                clientCommandsHandler.getApplyFullCollectionCommandData(), clientCommandsHandler.getSetAccountCommandData());
    }
}
