import collection.CollectionClassesHandler;
import commands.ClientCommandsHandler;
import threads.ClientInteractionController;
import web.ConnectionHandler;
import threads.ThreadHandler;
import commands.ExecutionController;
import commands.instances.*;
import console.ConsoleHandler;

import java.io.IOException;

/**
 * A Main class, only consists of main() method.
 */
public class Main {
    /**
     *
     */
    public static void main(String... args) {
        ConsoleHandler consoleHandler = new ConsoleHandler(System.in, System.out, System.err);
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler(consoleHandler);
            ClientCommandsHandler clientCommandsHandler = new ClientCommandsHandler();
            CollectionClassesHandler collectionClassesHandler = new CollectionClassesHandler();
            ExecutionController executionController = new ExecutionController(clientCommandsHandler, consoleHandler, collectionClassesHandler);
            ClientInteractionController app = new ClientInteractionController(executionController, consoleHandler);
            ThreadHandler threadHandler = new ThreadHandler(connectionHandler, executionController, app, consoleHandler);
            clientCommandsHandler.addCommands(
                    new Exit(consoleHandler, threadHandler),
                    new History(executionController),
                    new Help(executionController),
                    new ExecuteScript(app),
                    new FinishScript(app),
                    new Disconnect(connectionHandler, executionController),
                    new Connect(connectionHandler, executionController, threadHandler),
                    new ApplyCollectionChange(collectionClassesHandler),
                    new ApplyFullCollection(collectionClassesHandler),
                    new Show(collectionClassesHandler),
                    new Info(collectionClassesHandler),
                    new CountByColor(collectionClassesHandler),
                    new FilterByType(collectionClassesHandler),
                    new FilterGreaterThanAge(collectionClassesHandler));
            executionController.setThreadHandler(threadHandler);
            threadHandler.start();
        } catch (IOException | ClassNotFoundException e) {
            consoleHandler.errorMessage(e);
        }
    }
}
