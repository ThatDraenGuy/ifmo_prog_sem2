package threads;

import commands.ActionResult;
import console.ConsoleHandler;
import exceptions.ConnectionException;
import message.CommandResponse;
import message.Request;
import message.Response;
import web.ConnectionHandler;

import java.util.concurrent.Exchanger;

public class MessageSender {
    private final ConsoleHandler consoleHandler;
    private final ConnectionHandler connectionHandler;
    private final Exchanger<Response> responseExchanger;

    public MessageSender(ConnectionHandler connectionHandler, Exchanger<Response> responseExchanger, ConsoleHandler consoleHandler) {
        this.consoleHandler = consoleHandler;
        this.connectionHandler = connectionHandler;
        this.responseExchanger = responseExchanger;
    }

    public synchronized Response sendRequest(Request request) throws ConnectionException {
        consoleHandler.debugMessage("sending request...");
        connectionHandler.send(request);
        try {
            return responseExchanger.exchange(null);
        } catch (InterruptedException e) {
            return new CommandResponse(new ActionResult(false, "Couldn't get response"));
        }
    }
}
