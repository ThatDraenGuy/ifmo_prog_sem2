package server;

import commands.ServerCommandsHandler;
import message.ServerDataResponse;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.slf4j.Logger;

public class ServerHandler {
    private final ArrayList<UserHandler> users;
    private final RequestHandler requestHandler;
    private final ServerSocket server;
    private final Logger logger;
    final private int port = 2525;

    public ServerHandler(ServerCommandsHandler commandsHandler, ServerDataResponse serverData) throws IOException {
        server = new ServerSocket(port);
        this.requestHandler = new RequestHandler(commandsHandler, serverData);
        this.users = new ArrayList<>();
        logger = LoggerFactory.getLogger("server");
    }

    public void listen() throws IOException {
        try {
            while (true) {
                Socket user = server.accept();
                logger.info(user.getInetAddress() + " started connecting...");
                users.add(new UserHandler(user, requestHandler, logger, this));
                logger.info(user.getInetAddress() + " successfully connected");
            }
        } catch (IOException e) {
            logger.error(e.toString());
            //TODO change
        } finally {
            server.close();
        }
    }

    public void disconnect(UserHandler userHandler) {
        users.remove(userHandler);
    }
}
