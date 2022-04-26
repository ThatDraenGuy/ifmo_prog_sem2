import collection.*;
import collection.classes.DragonFactory;
import collection.classes.MainCollectible;
import collection.storage.database.DatabaseHandler;
import commands.CommandAccessLevel;
import commands.ServerCommandsHandler;
import commands.instances.*;
import collection.classes.Dragon;
import exceptions.IncorrectAccountDataException;
import exceptions.StorageException;
import exceptions.UnknownAccountException;
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

/**
 * A Main class, only consists of main() method.
 */
public class Main {
    final static Class<? extends MainCollectible<?>> target = Dragon.class;

    public static void main(String... args) {
        Logger logger = LoggerFactory.getLogger("loader");
        logger.info("Starting the server...");
        CurrentAccount.setAccount(new Account("server", "temp", CommandAccessLevel.DEV));
        //TODO config file?
        DatabaseHandler databaseHandler = null;
        AccountsHandler accountsHandler = null;
        try {
            databaseHandler = new DatabaseHandler();
            accountsHandler = new AccountsHandler(databaseHandler);
            accountsHandler.validate("guest", "guest");
            //TODO remove above
        } catch (SQLException | NoSuchAlgorithmException e) {
            logger.error(e.toString());
        } catch (IncorrectAccountDataException | UnknownAccountException | StorageException e) {
            e.printStackTrace();
        }

        DragonFactory factory = new DragonFactory();
        ServerCommandsHandler cmdHandler = new ServerCommandsHandler(accountsHandler);
        ServerHandler serverHandler = null;
        try {
            serverHandler = new ServerHandler(cmdHandler);
        } catch (IOException e) {
            logger.error("Error occurred while trying to start the server: " + e);
            System.exit(1);
        }
        ServerCollectionHandler<Dragon> collectionHandler = new ServerCollectionHandler<>(databaseHandler, serverHandler,
                factory, Dragon.class);
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
                new Shutdown(serverHandler),
                new Login(accountsHandler),
                new Register(accountsHandler));
        logger.info("Successfully loaded commands");
        ServerData serverData = new ServerData(cmdHandler.getCommandsData(), target);
        cmdHandler.setServerData(serverData);
        Thread serverThread = new Thread(serverHandler, "Server");
        serverThread.start();
    }
}
