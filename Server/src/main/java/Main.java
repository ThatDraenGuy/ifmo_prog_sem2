import collection.*;
import collection.classes.DragonFactory;
import collection.classes.MainCollectible;
import commands.CommandsHandler;
import commands.instances.*;
import collection.classes.Dragon;
import message.ServerDataResponse;
import server.ServerHandler;

import java.io.File;
import java.io.IOException;
import java.util.PriorityQueue;

/**
 * A Main class, only consists of main() method.
 */
public class Main {
    final static Class<? extends MainCollectible<?>> target = Dragon.class;
    final static String defaultPath = "DefaultCollection.json";

    public static void main(String... args) {
        String filePath;
        if (args.length != 1) {
            System.out.println("Input a filepath as jar's argument! Starting up with default collection...");
            filePath = defaultPath;
        } else {
            filePath = args[0];
        }
        File collectionFile = new File(filePath);
        if (!collectionFile.isFile()) {
            System.out.println("Can't find file \"" + filePath + "\". Starting up with default collection...");
            filePath = defaultPath;
        }
        StorageHandler storageHandler = new FileStorageHandler(new File(filePath), new JsonHandler());
        DragonFactory factory = new DragonFactory();
        CollectionHandler<Dragon> collectionHandler = new ConcreteCollectionHandler<>(storageHandler, new PriorityQueue<>(),
                factory, new DragonCollectionBuilder(factory), Dragon.class);
        CollectionBridge<Dragon> collectionBridge = new CollectionBridge<>(collectionHandler);
        if (!filePath.equals(defaultPath)) collectionHandler.load();
        CommandsHandler cmdHandler = new CommandsHandler();
        cmdHandler.addCommands(
                new Save(collectionHandler),
                new Show(collectionHandler),
                new RemoveFirst(collectionHandler),
                new RemoveById(collectionHandler),
                new Add(collectionBridge),
                new Update(collectionBridge),
                new Clear(collectionHandler),
                new RemoveLower(collectionBridge),
                new CountByColor(collectionHandler),
                new FilterByType(collectionHandler),
                new FilterGreaterThanAge(collectionHandler),
                new Info(collectionHandler));
        ServerDataResponse serverData = new ServerDataResponse(cmdHandler.getCommandsData(), target);
        try {
            ServerHandler serverHandler = new ServerHandler(cmdHandler, serverData);
            serverHandler.listen();
        } catch (IOException e) {
            System.out.println(e);
            //TODO
        }
    }
}
