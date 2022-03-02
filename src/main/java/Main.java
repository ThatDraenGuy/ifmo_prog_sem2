import Collection.Classes.Dragon;
import Collection.Classes.MainCollectible;
import Collection.CollectionHandler;
import Collection.StorageHandler;
import Console.ConsoleHandler;
import cmd.CmdHandler;
import cmd.commands.*;

import java.io.File;

public class Main {
    public static void main(String ... args) {
        String filePath;
        final String defaultFilePath = "./src/main/java/resources/DefaultCollection.json";
        final Class<? extends MainCollectible<?>> target = Dragon.class;
        if (args.length!=1) {
            System.out.println("Input a filepath as jar's argument! Starting up with default collection...");
            filePath=defaultFilePath;
        } else {
            filePath=args[0];
        }
        File collectionFile = new File(filePath);
        if (!collectionFile.isFile() && !collectionFile.getPath().equals(defaultFilePath)) {
            System.out.println("Can't find file \""+filePath+"\". Starting up with default collection...");
            collectionFile = new File(defaultFilePath);
        }
        StorageHandler storageHandler = new StorageHandler(collectionFile);
        CollectionHandler collectionHandler = new CollectionHandler(storageHandler, target);
        collectionHandler.load();
        CmdHandler cmdHandler = new CmdHandler(collectionHandler);
        cmdHandler.addCmds(new Help(cmdHandler),
                new Exit(),
                new Save(collectionHandler),
                new Show(collectionHandler),
                new RemoveFirst(collectionHandler),
                new RemoveById(collectionHandler),
                new Add(collectionHandler),
                new Update(collectionHandler),
                new Clear(collectionHandler),
                new RemoveLower(collectionHandler),
                new CountByColor(collectionHandler),
                new FilterByType(collectionHandler),
                new FilterGreaterThanAge(collectionHandler),
                new Info(collectionHandler),
                new ExecuteScript(cmdHandler),
                new History(cmdHandler));
        ConsoleHandler consoleHandler = new ConsoleHandler(cmdHandler, System.in, System.out, System.err);
        consoleHandler.start();
    }
}
