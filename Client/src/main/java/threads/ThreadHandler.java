package threads;

import commands.ExecutionController;
import lombok.Getter;
import web.ConnectionHandler;
import console.ConsoleHandler;
import message.Request;
import message.Response;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadHandler {
    @Getter
    private final Lock lock;
    @Getter
    private final Condition requestSent;
    @Getter
    private final Condition responseSent;
    private final ConnectionHandler connectionHandler;
    private final ClientInteractionController clientInteractionController;
    private final ConsoleHandler consoleHandler;
    private Thread requestListenerThread;
    private Thread messageReaderThread;
    @Getter
    private final MessageReader messageReader;
    private final RequestListener requestListener;


    public ThreadHandler(ConnectionHandler connectionHandler, ExecutionController executionController, ClientInteractionController clientInteractionController, ConsoleHandler consoleHandler) {
        this.connectionHandler = connectionHandler;
        this.clientInteractionController = clientInteractionController;
        this.consoleHandler = consoleHandler;
        this.lock = new ReentrantLock();
        requestSent = lock.newCondition();
        responseSent = lock.newCondition();
        this.messageReader = new MessageReader(this, connectionHandler, consoleHandler);
        this.requestListener = new RequestListener(this, consoleHandler, executionController);
    }

    public void start() {
        requestListenerThread = new Thread(requestListener, "requestListener");
        requestListenerThread.start();
        clientInteractionController.start();
    }

    //TODO think
    public synchronized Response sendRequest(Request request) {
        consoleHandler.debugMessage("sending request...");
        lock.lock();
        try {
            connectionHandler.send(request);
            responseSent.await();
//            synchronized (responseSent) {
//                responseSent.wait();
//            }
            return messageReader.getFreshResponseMessage().getData();
        } catch (InterruptedException e) {
            return null;
            //TODO
        } finally {
            lock.unlock();
        }
    }

    public void reloadMessageReader() {
        messageReaderThread = new Thread(messageReader, "messageReader");
        messageReaderThread.start();
    }

    public void stop() {
        connectionHandler.disconnect();
        requestListenerThread.interrupt();
        clientInteractionController.setExitQueried(true);
    }
}
