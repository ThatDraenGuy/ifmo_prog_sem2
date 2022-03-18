package server;

import commands.ServerCommandsHandler;
import message.*;

import java.util.HashMap;
import java.util.function.Function;

public class RequestHandler {
    private final HashMap<Class<? extends Request>, Function<Request, ? extends Response>> functionHashMap;
    private final ServerCommandsHandler commandsHandler;
    private final ServerDataResponse serverDataResponse;

    public RequestHandler(ServerCommandsHandler commandsHandler, ServerDataResponse serverDataResponse) {
        this.commandsHandler = commandsHandler;
        this.serverDataResponse = serverDataResponse;
        functionHashMap = new HashMap<>();
        functionHashMap.put(CommandRequest.class, this::handleCommandRequest);
        functionHashMap.put(ServerDataRequest.class, this::handleServerDataRequest);
    }

    public <T extends Request, E extends Response> E handle(T request, Class<E> ignoredClazz) {
        Function<Request, ? extends Response> function = functionHashMap.get(request.getClass());
        if (function != null) {
            return (E) function.apply(request);
        }
        return null;
        //TODO
    }

    public CommandResponse handleCommandRequest(Request request) {
        CommandRequest commandRequest = (CommandRequest) request;
        return commandsHandler.executeCommand(commandRequest);
    }

    public ServerDataResponse handleServerDataRequest(Request request) {
        return serverDataResponse;
    }

}
