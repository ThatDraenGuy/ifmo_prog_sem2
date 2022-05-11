package threads;

import commands.ExecutionController;
import lombok.Getter;
import web.ConnectionHandler;
import console.ConsoleHandler;
import message.Request;
import message.Response;

import java.util.concurrent.Exchanger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadHandler {
    private final ConnectionHandler connectionHandler;
    private final ClientInteractionController clientInteractionController;
    private final ConsoleHandler consoleHandler;
    private Thread messageReaderThread;
    private final MessageReader messageReader;


    public ThreadHandler(ConnectionHandler connectionHandler, MessageReader messageReader, ClientInteractionController clientInteractionController, ConsoleHandler consoleHandler) {
        this.connectionHandler = connectionHandler;
        this.clientInteractionController = clientInteractionController;
        this.consoleHandler = consoleHandler;
        this.messageReader = messageReader;
    }

    public void start() {
        clientInteractionController.start();
    }


    public void reloadMessageReader() {
        messageReaderThread = new Thread(messageReader, "messageReader");
        messageReaderThread.start();
    }

    public void stop() {
        connectionHandler.disconnect();
        messageReader.shutdown();
        clientInteractionController.setExitQueried(true);
    }
}
