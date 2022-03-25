import client.App;
import client.ConnectionHandler;
import collection.DragonCollectionBuilder;
import collection.classes.DragonFactory;
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
            App app = new App(connectionHandler, clientCommandsHandler, collectionBuilder, consoleHandler);
            clientCommandsHandler.addCommands(
                    new Exit(connectionHandler, consoleHandler),
                    new History(clientCommandsHandler),
                    new Help(clientCommandsHandler, connectionHandler.getServerData().getServerCommands()),
                    new ExecuteScript(app),
                    new FinishScript());
            app.start();
        } catch (IOException | ClassNotFoundException e) {
            consoleHandler.errorMessage(e);
        }
    }
}
