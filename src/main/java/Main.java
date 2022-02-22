import Collection.Classes.Dragon;
import Collection.CollectionHandler;
import Collection.StorageHandler;
import Console.ConsoleHandler;
import cmd.CmdHandler;
import cmd.commands.*;

import java.io.File;

public class Main {
    public static void main(String ... args) {
        String filePath = "./src/test/resources/testFile.json"; //TODO implement reading from args
        if (args.length!=1) {
            System.out.println("Input a filepath as jar's argument! Starting up with the default collection file...");
            filePath="./DefaultCollection.json";
        } else {
            filePath=args[0];
        }
        final Class target = Dragon.class;
        StorageHandler storageHandler = new StorageHandler(new File(filePath));
        CollectionHandler collectionHandler = new CollectionHandler(storageHandler, target);
        collectionHandler.load();
        CmdHandler cmdHandler = new CmdHandler();
        cmdHandler.addCmds(new Help(cmdHandler),
                new Exit(),
                new Save(collectionHandler),
                new Show(collectionHandler),
                new RemoveFirst(collectionHandler),
                new RemoveById(collectionHandler),
                new Add(collectionHandler),
                new Update(collectionHandler));
        ConsoleHandler consoleHandler = new ConsoleHandler(collectionHandler, cmdHandler);
        consoleHandler.start();
    }
}
