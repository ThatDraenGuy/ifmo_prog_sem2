import commands.ServerCommandsHandler;
import commands.instances.*;
import collection.CollectionHandler;
import collection.StorageHandler;
import collection.classes.Dragon;

import java.io.File;

/**
 * A Main class, only consists of main() method.
 */
public class Main {
    /**
     *
     */
    public static void main(String... args) {
        String filePath;
        String defaultPath = "DefaultCollection.json";
        final Class<? extends MainCollectible<?>> target = Dragon.class;
        if (args.length != 1) {
            System.out.println("Input a filepath as jar's argument! Starting up with default collection...");
            filePath = defaultPath;
        } else {
            filePath = args[0];
        }
        File collectionFile = new File(filePath);
        if (!collectionFile.isFile() && !filePath.equals(defaultPath)) {
            System.out.println("Can't find file \"" + filePath + "\". Starting up with default collection...");
            filePath = defaultPath;
        }
        StorageHandler storageHandler = new StorageHandler(new File(filePath));
        CollectionHandler collectionHandler = new CollectionHandler(storageHandler, target);
        if (!filePath.equals(defaultPath)) collectionHandler.load();
        ServerCommandsHandler cmdHandler = new ServerCommandsHandler(collectionHandler);
        cmdHandler.addCommands(new Help(cmdHandler),
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
    }
}
