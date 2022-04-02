import commands.ClientCommandsHandler;
import threads.ClientInteractionController;
import web.ConnectionHandler;
import threads.ThreadHandler;
import collection.DragonCollectionBuilder;
import collection.classes.DragonFactory;
import commands.ExecutionController;
import commands.CommandsHandler;
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
            ConnectionHandler connectionHandler = new ConnectionHandler("127.0.0.1", 2525, consoleHandler);
            ClientCommandsHandler clientCommandsHandler = new ClientCommandsHandler();
            DragonCollectionBuilder collectionBuilder = new DragonCollectionBuilder(new DragonFactory());
            ExecutionController executionController = new ExecutionController(clientCommandsHandler, consoleHandler);
            ClientInteractionController app = new ClientInteractionController(executionController, collectionBuilder, consoleHandler);
            ThreadHandler threadHandler = new ThreadHandler(connectionHandler, executionController, app, consoleHandler);
            clientCommandsHandler.addCommands(
                    new Exit(consoleHandler, executionController),
                    new History(executionController),
                    new Help(executionController),
                    new ExecuteScript(app),
                    new FinishScript(),
                    new Disconnect(connectionHandler, executionController),
                    new Connect(connectionHandler, executionController, threadHandler));
            //TODO think below
            executionController.setThreadHandler(threadHandler);
            threadHandler.start();
        } catch (IOException | ClassNotFoundException e) {
            consoleHandler.errorMessage(e);
        }
    }
}
