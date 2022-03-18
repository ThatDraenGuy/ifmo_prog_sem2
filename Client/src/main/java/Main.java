import client.ConnectionHandler;
import commands.CommandsHandler;
import commands.instances.ExecuteScript;
import commands.instances.Exit;
import commands.instances.Help;
import commands.instances.History;
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
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler("127.0.0.1", 2525);
            CommandsHandler clientCommandsHandler = new CommandsHandler();
            ConsoleHandler consoleHandler = new ConsoleHandler(connectionHandler, clientCommandsHandler, System.in, System.out, System.err);
            clientCommandsHandler.addCommands(
                    new Exit(),
                    new History(clientCommandsHandler),
                    new Help(clientCommandsHandler),
                    new ExecuteScript(connectionHandler, clientCommandsHandler));
            consoleHandler.start();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
        }

    }
}
