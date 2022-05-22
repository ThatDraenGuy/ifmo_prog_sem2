package threads;

import app.App;
import javafx.application.Platform;
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
//        clientInteractionController.start();
        App app = new App();
        app.main();
    }


    public void reloadMessageReader() {
        messageReaderThread = new Thread(messageReader, "messageReader");
        messageReaderThread.start();
    }

    public void stop() {
        consoleHandler.message("stopping...");
        connectionHandler.disconnect();
        messageReader.shutdown();
        clientInteractionController.setExitQueried(true);
        Platform.exit();
        System.exit(0);
    }
}
