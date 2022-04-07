package threads;

import commands.ExecutionController;
import console.ConsoleHandler;
import exceptions.CommandArgsAmountException;

public class RequestListener implements Runnable {
    private final ConsoleHandler consoleHandler;
    private final ExecutionController executionController;
    private final ThreadHandler threadHandler;

    public RequestListener(ThreadHandler threadHandler, ConsoleHandler consoleHandler, ExecutionController executionController) {
        this.threadHandler = threadHandler;
        this.consoleHandler = consoleHandler;
        this.executionController = executionController;
    }

    @Override
    public void run() {
        final Runnable commandsExecutor = () -> {
            try {
                executionController.executeCommand(threadHandler.getMessageReader().getFreshRequestMessage().getData());
            } catch (CommandArgsAmountException ignored) {
            }
        };
        while (true) {
            consoleHandler.debugMessage("requestListener started");
            try {
                synchronized (threadHandler.getRequestListenerLock()) {
                    threadHandler.getRequestListenerLock().wait();
                    Thread executionThread = new Thread(commandsExecutor, "executionThread");
                    executionThread.start();
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
