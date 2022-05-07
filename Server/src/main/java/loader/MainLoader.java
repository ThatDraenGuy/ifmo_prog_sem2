package loader;

import collection.*;
import collection.classes.DragonFactory;
import collection.classes.MainCollectible;
import collection.storage.database.DatabaseHandler;
import commands.CommandAccessLevel;
import commands.ServerCommandsHandler;
import commands.instances.*;
import collection.classes.Dragon;
import message.ServerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.Account;
import security.AccountsHandler;
import security.CurrentAccount;
import web.ServerHandler;
import web.UserDataHandler;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A loader.Main class, only consists of main() method.
 */
public class MainLoader {
    final static Class<? extends MainCollectible<?>> target = Dragon.class;

    public static void main(String... args) {
        Logger logger = LoggerFactory.getLogger("loader");
        logger.info("Starting the server...");
        ConfigLoader configLoader = new ConfigLoader();
        Properties config = configLoader.load();
        CurrentAccount.setAccount(new Account(config.getProperty("username"), config.getProperty("password"), CommandAccessLevel.DEV));
        DatabaseHandler databaseHandler = null;
        AccountsHandler accountsHandler = null;
        try {
            databaseHandler = new DatabaseHandler(config);
            accountsHandler = new AccountsHandler(databaseHandler);
        } catch (SQLException | NoSuchAlgorithmException e) {
            logger.error(e.toString());
            System.exit(1);
        }

        DragonFactory factory = new DragonFactory();
        ServerCommandsHandler cmdHandler = new ServerCommandsHandler(accountsHandler);
        ServerHandler serverHandler = null;
        try {
            serverHandler = new ServerHandler(cmdHandler, config.getProperty("port"));
        } catch (IOException e) {
            logger.error("Error occurred while trying to start the server: " + e);
            e.printStackTrace();
            System.exit(1);
        } catch (NumberFormatException e) {
            logger.error("Port is not valid: " + config.getProperty("port"));
            System.exit(1);
        }
        ServerCollectionHandler<Dragon> collectionHandler = new ServerCollectionHandler<>(databaseHandler, serverHandler,
                factory, Dragon.class);
        UserDataHandler.setCollectionHandler(collectionHandler);
        collectionHandler.load();

        cmdHandler.addCommands(
                new Save(collectionHandler),
                new RemoveFirst(collectionHandler),
                new RemoveById(collectionHandler),
                new Add(collectionHandler),
                new Update(collectionHandler),
                new Clear(collectionHandler),
                new RemoveLower(collectionHandler),
                new FetchServerData(),
                new StopServer(serverHandler),
                new Shutdown(serverHandler),
                new Login(accountsHandler),
                new Register(accountsHandler),
                new Disconnect());
        logger.info("Successfully loaded commands");
        ServerData serverData = new ServerData(cmdHandler.getCommandsData(), target);
        cmdHandler.setServerData(serverData);
        Thread serverThread = new Thread(serverHandler, "Server");
        serverThread.start();
    }
}
