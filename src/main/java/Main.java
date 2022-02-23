import Collection.Classes.Dragon;
import Collection.CollectionHandler;
import Collection.StorageHandler;
import Console.ConsoleHandler;
import cmd.CmdHandler;
import cmd.commands.*;

import java.io.File;

public class Main {
    public static void main(String ... args) {
        //TODO ROADMAP: -null fields in an element construction; -show enum values when saying that value is not from enum;
        //TODO -execute_script situations handling; -fix the input prompt appearing before some stuff??; -javadoc; remake JSONToCollection
        String filePath;
        final String defaultFilePath = "./DefaultCollection.json";
        if (args.length!=1) {
            System.out.println("Input a filepath as jar's argument! Starting up with the default collection file...");
            filePath=defaultFilePath;
        } else {
            filePath=args[0];
        }
        File collectionFile = new File(filePath);
        if (!collectionFile.isFile()) {
            System.out.println("Can't find file \""+filePath+"\". Starting up with the default collection file...");
            collectionFile = new File(defaultFilePath);
        }
        final Class<?> target = Dragon.class;
        StorageHandler storageHandler = new StorageHandler(collectionFile);
        CollectionHandler collectionHandler = new CollectionHandler(storageHandler, target);
        collectionHandler.load();
        CmdHandler cmdHandler = new CmdHandler(collectionHandler);
        //TODO think about above
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
