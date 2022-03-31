import client.App;
import client.ConnectionHandler;
import collection.DragonCollectionBuilder;
import collection.classes.DragonFactory;
import commands.CommandsExecutor;
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
            CommandsHandler clientCommandsHandler = new CommandsHandler();
            DragonCollectionBuilder collectionBuilder = new DragonCollectionBuilder(new DragonFactory());
            CommandsExecutor commandsExecutor = new CommandsExecutor(connectionHandler, clientCommandsHandler, consoleHandler);
            App app = new App(commandsExecutor, collectionBuilder, consoleHandler);
            clientCommandsHandler.addCommands(
                    new Exit(consoleHandler, commandsExecutor),
                    new History(commandsExecutor),
                    new Help(commandsExecutor),
                    new ExecuteScript(app),
                    new FinishScript());
            app.start();
        } catch (IOException | ClassNotFoundException e) {
            consoleHandler.errorMessage(e);
        }
    }
}
