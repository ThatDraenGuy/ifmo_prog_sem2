package threads;

import commands.ExecutionController;
import exceptions.CommandArgsAmountException;
import lombok.Getter;
import web.ConnectionHandler;
import commands.CommandsHandler;
import console.ConsoleHandler;
import message.Request;
import message.Response;

public class ThreadHandler {
    @Getter
    private final Object requestListenerLock = new Object();
    @Getter
    private final Object responseListenerLock = new Object();
    //    private Message<Request> freshRequestMessage;
//    private Message<Response> freshResponseMessage;
    private final ConnectionHandler connectionHandler;
    private final ExecutionController executionController;
    private final ClientInteractionController clientInteractionController;
    private final ConsoleHandler consoleHandler;
    private Thread requestListenerThread;
    private Thread messageReaderThread;
    private final MessageReader messageReader;


    public ThreadHandler(ConnectionHandler connectionHandler, ExecutionController executionController, ClientInteractionController clientInteractionController, ConsoleHandler consoleHandler) {
        this.connectionHandler = connectionHandler;
        this.executionController = executionController;
        this.clientInteractionController = clientInteractionController;
        this.consoleHandler = consoleHandler;
        this.messageReader = new MessageReader(this, connectionHandler, consoleHandler);
//        freshRequestMessage = null;
//        freshResponseMessage = null;
    }

    public void start() {
        final Runnable requestListener = () -> {
            while (true) {
                consoleHandler.debugMessage("requestListener started");
                try {
                    synchronized (requestListenerLock) {
                        requestListenerLock.wait();
                        executionController.executeCommand(messageReader.getFreshRequestMessage().getData());
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (CommandArgsAmountException ignored) {
                }
//                try {
//                    Message<Request> requestMessage = connectionHandler.readMessage();
//                    commandsHandler.executeCommand(requestMessage.getData());
//                } catch (IOException | ClassNotFoundException e) {
//                    //TODO
//                }
            }
        };
        requestListenerThread = new Thread(requestListener, "requestListener");
//        reloadMessageReader();
        requestListenerThread.start();
        clientInteractionController.start();
    }

    //TODO think
    public synchronized Response sendRequest(Request request) {
        consoleHandler.debugMessage("sending request...");
        try {
            connectionHandler.send(request);
            synchronized (responseListenerLock) {
                responseListenerLock.wait();
            }
            return messageReader.getFreshResponseMessage().getData();
        } catch (InterruptedException e) {
            return null;
            //TODO
        }
    }

    public void reloadMessageReader() {
//        final Runnable messageReader = () -> {
//            while (connectionHandler.isConnectionOpen()) {
//                consoleHandler.debugMessage("messageReader started");
//                try {
//                    Message<?> message = connectionHandler.readMessage();
//                    consoleHandler.debugMessage("I got a message!");
//                    if (message.getData() instanceof Request) {
//                        freshRequestMessage = (Message<Request>) message;
//                        synchronized (requestListenerLock) {
//                            requestListenerLock.notify();
//                        }
//                    } else {
//                        freshResponseMessage = (Message<Response>) message;
//                        synchronized (responseListenerLock) {
//                            responseListenerLock.notify();
//                        }
//                    }
//                } catch (EOFException e) {
//                    connectionHandler.handleLostConnection();
//                } catch (IOException | ClassNotFoundException e) {
//                    consoleHandler.errorMessage(e);
//                }
//            }
//        };
        messageReaderThread = new Thread(messageReader, "messageReader");
        messageReaderThread.start();
    }

    public void stop() {
        connectionHandler.disconnect();
        requestListenerThread.interrupt();
    }
}
