import collection.CollectionHandler;
import collection.StorageHandler;
import collection.classes.Dragon;
import collection.classes.MainCollectible;
import console.ConsoleHandler;

/**
 * A Main class, only consists of main() method.
 */
public class Main {
    /**
     *
     */
    public static void main(String... args) {
        ConsoleHandler consoleHandler = new ConsoleHandler(cmdHandler, System.in, System.out, System.err);
        consoleHandler.start();
    }
}
