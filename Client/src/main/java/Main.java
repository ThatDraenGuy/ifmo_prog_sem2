import client.ConnectionHandler;
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
        try {
            ConnectionHandler connectionHandler = new ConnectionHandler("127.0.0.1", 2525);
            CommandsHandler clientCommandsHandler = new CommandsHandler();
            clientCommandsHandler.addCommands(
                    new Exit(connectionHandler),
                    new History(clientCommandsHandler),
                    new Help(clientCommandsHandler, connectionHandler.getServerData().getServerCommands()),
                    new ExecuteScript(connectionHandler, clientCommandsHandler),
                    new FinishScript());
            ConsoleHandler consoleHandler = new ConsoleHandler(connectionHandler, clientCommandsHandler, System.in, System.out, System.err);
            consoleHandler.start();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
        }

    }
}
