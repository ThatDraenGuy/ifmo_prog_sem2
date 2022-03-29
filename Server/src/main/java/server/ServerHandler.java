package server;

import commands.CommandsHandler;
import message.ServerData;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

public class ServerHandler {
    private final List<UserHandler> users;
    private final CommandsHandler commandsHandler;
    private final ServerSocket server;
    private final Logger logger;
    final private int port = 2525;
    private boolean shutdownFlag = false;

    public ServerHandler(CommandsHandler commandsHandler) throws IOException {
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

    public void shutdown() {
        logger.info("Shutting down the server...");
//        users.forEach(this::disconnect);
        System.exit(0);
    }
}
