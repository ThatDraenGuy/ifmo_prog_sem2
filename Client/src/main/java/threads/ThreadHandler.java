package threads;

import web.ConnectionHandler;
import console.ConsoleHandler;

public class ThreadHandler {
    private final ConnectionHandler connectionHandler;
    private final ConsoleInteractionController clientInteractionController;
    private final ConsoleHandler consoleHandler;
    private Thread messageReaderThread;
    private final MessageReader messageReader;


    public ThreadHandler(ConnectionHandler connectionHandler, MessageReader messageReader, ConsoleInteractionController clientInteractionController, ConsoleHandler consoleHandler) {
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
