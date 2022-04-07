import collection.*;
import collection.classes.DragonFactory;
import collection.classes.MainCollectible;
import commands.ServerCommandsHandler;
import commands.instances.*;
import collection.classes.Dragon;
import message.ServerData;
import web.ServerHandler;
import web.UserDataHandler;

import java.io.File;
import java.io.IOException;

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
        ServerCommandsHandler cmdHandler = new ServerCommandsHandler();
        ServerHandler serverHandler = null;
        try {
            serverHandler = new ServerHandler(cmdHandler);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
            //TODO
        }
        ServerCollectionHandler<Dragon> collectionHandler = new ServerCollectionHandler<>(storageHandler, serverHandler,
                factory, new DragonCollectionBuilder(factory), Dragon.class);
        UserDataHandler.setCollectionHandler(collectionHandler);
        CollectionBridge<Dragon> collectionBridge = new CollectionBridge<>(collectionHandler);
        collectionHandler.load();

        cmdHandler.addCommands(
                new Save(collectionHandler),
//                new Show(collectionHandler),
                new RemoveFirst(collectionHandler),
                new RemoveById(collectionHandler),
                new Add(collectionBridge),
                new Update(collectionBridge),
                new Clear(collectionHandler),
                new RemoveLower(collectionBridge),
                new CountByColor(collectionHandler),
                new FilterByType(collectionHandler),
                new FilterGreaterThanAge(collectionHandler),
                new Info(collectionHandler),
                new FetchServerData(),
                new StopServer(serverHandler));
        ServerData serverData = new ServerData(cmdHandler.getCommandsData(), target);
        cmdHandler.setServerData(serverData);
        serverHandler.listen();
    }
}
