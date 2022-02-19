import Collection.CollectionHandler;
import Collection.StorageHandler;
import Console.ConsoleHandler;
import cmd.CmdHandler;
import cmd.commands.*;

import java.io.File;

public class Main {
    public static void main(String ... args) {
        String filePath = "./src/test/resources/testFile.json"; //TODO implement reading from args
        StorageHandler storageHandler = new StorageHandler(new File(filePath));
        CollectionHandler collectionHandler = new CollectionHandler(storageHandler);
        collectionHandler.load();
        CmdHandler cmdHandler = new CmdHandler();
        cmdHandler.addCmds(new Help(cmdHandler),
                new Exit(),
                new Save(collectionHandler),
                new Show(collectionHandler),
                new RemoveFirst(collectionHandler),
                new RemoveById(collectionHandler));
        ConsoleHandler consoleHandler = new ConsoleHandler(collectionHandler, cmdHandler);
        consoleHandler.start();
    }
}
