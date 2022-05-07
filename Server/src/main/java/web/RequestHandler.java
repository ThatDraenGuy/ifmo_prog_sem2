package web;

import commands.ServerCommandsHandler;
import message.Request;
import message.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Response> {
    private final Request request;
    private final UserHandler userHandler;
    private final ServerCommandsHandler commandsHandler;
    private final Logger logger;

    public RequestHandler(Request request, UserHandler userHandler, ServerCommandsHandler commandsHandler) {
        this.request = request;
        this.userHandler = userHandler;
        this.commandsHandler = commandsHandler;
        this.logger = LoggerFactory.getLogger("server.requestHandler");
    }

    @Override
    public Response call() {
        logger.debug("Handling request...");
        return commandsHandler.executeCommand(request, userHandler);
    }
}
