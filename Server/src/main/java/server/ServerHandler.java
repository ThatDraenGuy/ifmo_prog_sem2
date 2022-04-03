package server;

import commands.ServerCommandsHandler;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

public class ServerHandler {
    private final List<UserHandler> users;
    private final ServerCommandsHandler commandsHandler;
    private final ServerSocket server;
    private final Logger logger;
    final private int port = 2525;
    private boolean shutdownFlag = false;

    public ServerHandler(ServerCommandsHandler commandsHandler) throws IOException {
        server = new ServerSocket(port);
        this.commandsHandler = commandsHandler;
        this.users = new ArrayList<>();
        logger = LoggerFactory.getLogger("server");
    }

    public void listen() {
        while (true) {
            try {
                Socket user = server.accept();
                logger.info(user.getInetAddress() + " started connecting...");
                users.add(new UserHandler(user, commandsHandler, logger, this));
                logger.info(user.getInetAddress() + " successfully connected");
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }

    public void disconnect(UserHandler userHandler) {
//        userHandler.disconnect();
        users.remove(userHandler);
    }

    public void silentDisconnect(UserHandler userHandler) {
//        userHandler.silentDisconnect();
        users.remove(userHandler);
    }

    public void stopServer() {
        final Runnable stopServer = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            logger.info("Disconnecting all users...");
            users.forEach(UserHandler::forceDisconnect);
            users.forEach(this::disconnect);
            logger.debug(users.toString());
        };
        Thread stopThread = new Thread(stopServer, "stopThread");
        stopThread.start();
    }
}
