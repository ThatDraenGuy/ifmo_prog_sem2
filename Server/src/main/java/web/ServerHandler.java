package web;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import commands.CommandArgs;
import commands.CommandData;
import commands.ServerCommandsHandler;
import message.CommandRequest;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import utility.LimitedCollection;

public class ServerHandler {
    private final List<UserHandler> users;
    private final ServerCommandsHandler commandsHandler;
    private final ServerSocket server;
    private final Logger logger;
    final private int port = 2525;

    public ServerHandler(ServerCommandsHandler commandsHandler) throws IOException {
        server = new ServerSocket(port);
        this.commandsHandler = commandsHandler;
        this.users = new ArrayList<>();
        logger = LoggerFactory.getLogger("server");
    }

    public void listen() {
        while (!server.isClosed()) {
            try {
                Socket user = server.accept();
                logger.info(user.getInetAddress() + " started connecting...");
                users.add(new UserHandler(user, commandsHandler, logger, this));
                logger.info(user.getInetAddress() + " successfully connected");

            } catch (IOException e) {
                if (!server.isClosed()) logger.error(e.toString());
            }
        }
    }

    public void disconnect(UserHandler userHandler) {
        users.remove(userHandler);
    }

    private void closeSocket() {
        try {
            server.close();
        } catch (IOException ignored) {
        }
    }

    public void stopServer() {
        final Runnable stopServer = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            logger.info("Disconnecting all users...");
            users.forEach(UserHandler::forceDisconnect);
            users.clear();
            logger.info("Saving collection...");
            commandsHandler.executeCommand(new CommandRequest(commandsHandler.getSaveCommandData(), new CommandArgs("")));
            logger.info("exiting...");
            closeSocket();
        };
        Thread stopThread = new Thread(stopServer, "stoppingThread");
        stopThread.start();
    }

    public void shutdown() {
        System.exit(0);
    }

    public void sendCollectionChanges(Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges) {
        users.forEach(userHandler -> userHandler.sendCollectionChangeRequest(collectionChanges));
    }

}
