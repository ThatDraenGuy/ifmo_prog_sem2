package web;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import commands.CommandArgs;
import commands.ServerCommandsHandler;
import message.CommandRequest;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;

public class ServerHandler implements Runnable {
    private final List<UserHandler> users;
    private final ServerCommandsHandler commandsHandler;
    private final Logger logger;
    final private int port = 2525;
    //
    private final ServerSocketChannel serverSocketChannel;

    public ServerHandler(ServerCommandsHandler commandsHandler) throws IOException {
        this.commandsHandler = commandsHandler;
        this.users = new ArrayList<>();
        logger = LoggerFactory.getLogger("server");
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
    }

    public void run() {
        logger.info("Started server");
        while (serverSocketChannel.isOpen()) {
            try {
                SocketChannel userChannel = serverSocketChannel.accept();
                logger.info(userChannel.socket().getInetAddress() + " started connecting...");
                users.add(new UserHandler(userChannel, commandsHandler, this));
                logger.info(userChannel.socket().getInetAddress() + " successfully connected");
            } catch (IOException e) {
                if (serverSocketChannel.isOpen()) logger.error(e.toString());
            }
        }
    }

    public void disconnect(UserHandler userHandler) {
        users.remove(userHandler);
    }

    private void closeChannel() {
        try {
            serverSocketChannel.close();
        } catch (IOException ignored) {
        }
    }

    public void stopServer() {
        final Runnable stopServer = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            logger.info("Stopping the server...");
            logger.info("Disconnecting all users...");
            users.forEach(UserHandler::forceDisconnect);
            users.clear();
            closeChannel();
            logger.info("Saving collection...");
            commandsHandler.executeCommand(new CommandRequest(commandsHandler.getSaveCommandData(), new CommandArgs("")));
            logger.info("exiting...");
        };
        Thread stopThread = new Thread(stopServer, "stoppingThread");
        stopThread.start();
    }

    public void shutdown() {
        logger.info("shutdown");
        System.exit(0);
    }

    public void sendCollectionChanges(Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges) {
        users.forEach(userHandler -> userHandler.sendCollectionChangeRequest(collectionChanges));
    }

}
