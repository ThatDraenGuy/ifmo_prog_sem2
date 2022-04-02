package threads;

import console.ConsoleHandler;
import lombok.Getter;
import message.Message;
import message.Request;
import message.Response;
import web.ConnectionHandler;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

public class MessageReader implements Runnable {
    private final ConnectionHandler connectionHandler;
    private final ConsoleHandler consoleHandler;
    @Getter
    private Message<Request> freshRequestMessage;
    @Getter
    private Message<Response> freshResponseMessage;
    private final ThreadHandler threadHandler;

    public MessageReader(ThreadHandler threadHandler, ConnectionHandler connectionHandler, ConsoleHandler consoleHandler) {
        this.threadHandler = threadHandler;
        this.connectionHandler = connectionHandler;
        this.consoleHandler = consoleHandler;
        freshRequestMessage = null;
        freshResponseMessage = null;
    }

    @Override
    public void run() {
        while (connectionHandler.isConnectionOpen()) {
            consoleHandler.debugMessage("messageReader started");
            try {
                Message<?> message = connectionHandler.readMessage();
                consoleHandler.debugMessage("I got a message!");
                if (message.getData() instanceof Request) {
                    freshRequestMessage = (Message<Request>) message;
                    synchronized (threadHandler.getRequestListenerLock()) {
                        threadHandler.getRequestListenerLock().notify();
                    }
                } else {
                    freshResponseMessage = (Message<Response>) message;
                    synchronized (threadHandler.getResponseListenerLock()) {
                        threadHandler.getResponseListenerLock().notify();
                    }
                }
            } catch (EOFException e) {
                connectionHandler.handleLostConnection();
//            } catch (SocketException ignored) {

            } catch (IOException | ClassNotFoundException e) {
                consoleHandler.errorMessage(e);
            }
        }
    }
}
