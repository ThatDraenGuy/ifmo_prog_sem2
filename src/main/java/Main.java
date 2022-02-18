import Collection.CollectionHandler;
import Collection.StorageHandler;
import Console.ConsoleHandler;
import cmd.CmdHandler;
import cmd.commands.Help;

import java.io.File;

public class Main {
    public static void main(String ... args) {
        String filePath = "./src/test/resources/testFile.json"; //TODO implement reading from args
        StorageHandler storageHandler = new StorageHandler(new File(filePath));
        CollectionHandler collectionHandler = new CollectionHandler(storageHandler);
        collectionHandler.load();
        CmdHandler cmdHandler = new CmdHandler();
        cmdHandler.addCmds(new Help(cmdHandler));
        ConsoleHandler consoleHandler = new ConsoleHandler(collectionHandler, cmdHandler);
        consoleHandler.start();
    }
}
