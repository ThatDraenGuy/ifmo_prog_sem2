package server;

import commands.CommandsHandler;
import message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.function.Function;

public class RequestHandler {
    private final HashMap<Class<? extends Request>, Function<Request, ? extends Response>> functionHashMap;
    private final CommandsHandler commandsHandler;
    private final ServerDataResponse serverDataResponse;
    private final Logger logger;

    public RequestHandler(CommandsHandler commandsHandler, ServerDataResponse serverDataResponse) {
        this.commandsHandler = commandsHandler;
        this.serverDataResponse = serverDataResponse;
        this.logger = LoggerFactory.getLogger("RequestHandler");
        functionHashMap = new HashMap<>();
        functionHashMap.put(CommandRequest.class, this::handleCommandRequest);
        functionHashMap.put(ServerDataRequest.class, this::handleServerDataRequest);
        functionHashMap.put(DisconnectRequest.class, this::handleDisconnectRequest);
    }

    public <T extends Request, E extends Response> E handle(T request, Class<E> ignoredClazz) {
        Function<Request, ? extends Response> function = functionHashMap.get(request.getClass());
        if (function != null) {
            return (E) function.apply(request);
        }
        return null;
        //TODO
    }

    private CommandResponse handleCommandRequest(Request request) {
        CommandRequest commandRequest = (CommandRequest) request;
        logger.debug("Request for executing command " + commandRequest.getCommandData().getName());
        return commandsHandler.executeCommand(commandRequest);
    }

    private ServerDataResponse handleServerDataRequest(Request request) {
        return serverDataResponse;
    }

    private DisconnectResponse handleDisconnectRequest(Request request) {
        return new DisconnectResponse();
    }

}
