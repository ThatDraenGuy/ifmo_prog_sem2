import client.ConnectionHandler;
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
            ConsoleHandler consoleHandler = new ConsoleHandler(connectionHandler, System.in, System.out, System.err);
            consoleHandler.start();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
        }

    }
}
