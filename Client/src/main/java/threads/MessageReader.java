package threads;

import commands.ActionResult;
import commands.ExecutionController;
import console.ConsoleHandler;
import exceptions.CommandArgsAmountException;
import exceptions.ConnectionException;
import message.CommandResponse;
import message.Message;
import message.Request;
import message.Response;
import web.ConnectionHandler;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.*;

public class MessageReader implements Runnable {
    private final ConnectionHandler connectionHandler;
    private final ConsoleHandler consoleHandler;
    private final ExecutionController executionController;
    private final ExecutorService executor;
    private final Exchanger<Response> responseExchanger;

    public MessageReader(Exchanger<Response> exchanger, ConnectionHandler connectionHandler, ExecutionController executionController, ConsoleHandler consoleHandler) {
        this.connectionHandler = connectionHandler;
        this.consoleHandler = consoleHandler;
        this.executionController = executionController;
        this.responseExchanger = exchanger;
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void run() {
        while (connectionHandler.isConnectionOpen()) {
            consoleHandler.debugMessage("messageReader started");
            try {
                Message<?> message = connectionHandler.readMessage();
                consoleHandler.debugMessage("I got a message!");
                if (message.getData() instanceof Request request) {
                    final Runnable commandsExecutor = () -> {
                        try {
                            executionController.executeCommand(request);
                        } catch (CommandArgsAmountException | ConnectionException ignored) {
                        }
                    };
                    executor.execute(commandsExecutor);
                }
                if (message.getData() instanceof Response response) {
                    returnResponse(response);
                }
            } catch (EOFException e) {
                if (!connectionHandler.isConnectionClosed()) connectionHandler.handleLostConnection();
            } catch (SocketException e) {
                consoleHandler.message("(connection terminated)");
                returnResponse(null);
//                executor.shutdown();
            } catch (IOException | ClassNotFoundException e) {
                consoleHandler.errorMessage(e);
            }
        }
    }

    private void returnResponse(Response response) {
        try {
            responseExchanger.exchange(response, 500, TimeUnit.MILLISECONDS);
        } catch (TimeoutException | InterruptedException ignored) {
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
