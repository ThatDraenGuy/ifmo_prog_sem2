import collection.*;
import collection.classes.DragonFactory;
import collection.classes.MainCollectible;
import collection.storage.database.DatabaseHandler;
import collection.storage.FileStorageHandler;
import collection.storage.JsonHandler;
import collection.storage.StorageHandler;
import commands.ServerCommandsHandler;
import commands.instances.*;
import collection.classes.Dragon;
import message.ServerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.ServerHandler;
import web.UserDataHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * A Main class, only consists of main() method.
 */
public class Main {
    final static Class<? extends MainCollectible<?>> target = Dragon.class;
    final static String defaultPath = "DefaultCollection.json";

    public static void main(String... args) {
        Logger logger = LoggerFactory.getLogger("loader");
        logger.info("Starting the server...");
        try {
            DatabaseHandler databaseHandler = new DatabaseHandler();
        } catch (SQLException e) {
            logger.error(e.toString());
        }


        String filePath;
        if (args.length != 1) {
            logger.warn("Filepath wasn't inputted; starting up with default collection");
            filePath = defaultPath;
        } else {
            filePath = args[0];
        }
        File collectionFile = new File(filePath);
        if (!collectionFile.isFile()) {
            logger.warn("Couldn't find file \"" + filePath + "\". Starting up with empty collection...");
            filePath = defaultPath;
        }
        StorageHandler storageHandler = new FileStorageHandler(new File(filePath), new JsonHandler());
        DragonFactory factory = new DragonFactory();
        ServerCommandsHandler cmdHandler = new ServerCommandsHandler();
        ServerHandler serverHandler = null;
        try {
            serverHandler = new ServerHandler(cmdHandler);
        } catch (IOException e) {
            logger.error("Error occurred while trying to start the server: " + e);
            System.exit(1);
        }
        ServerCollectionHandler<Dragon> collectionHandler = new ServerCollectionHandler<>(storageHandler, serverHandler,
                factory, new DragonCollectionBuilder(factory), Dragon.class);
        UserDataHandler.setCollectionHandler(collectionHandler);
        CollectionBridge<Dragon> collectionBridge = new CollectionBridge<>(collectionHandler);
        collectionHandler.load();

        cmdHandler.addCommands(
                new Save(collectionHandler),
                new RemoveFirst(collectionHandler),
                new RemoveById(collectionHandler),
                new Add(collectionBridge),
                new Update(collectionBridge),
                new Clear(collectionHandler),
                new RemoveLower(collectionBridge),
                new FetchServerData(),
                new StopServer(serverHandler),
                new Shutdown(serverHandler));
        logger.info("Successfully loaded commands");
        ServerData serverData = new ServerData(cmdHandler.getCommandsData(), target);
        cmdHandler.setServerData(serverData);
        Thread serverThread = new Thread(serverHandler, "Server");
        serverThread.start();
    }
}
