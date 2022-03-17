package server;

import commands.ServerCommandsHandler;
import message.ServerData;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.slf4j.Logger;

public class ServerHandler {
    private ArrayList<UserHandler> users;
    private ServerCommandsHandler commandsHandler;
    private ServerData serverData;
    private ServerSocket server;
    private Logger logger;
    final private int port = 2525;

    public ServerHandler(ServerCommandsHandler commandsHandler, ServerData serverData) throws IOException {
        server = new ServerSocket(port);
        this.commandsHandler = commandsHandler;
        this.serverData = serverData;
        this.users = new ArrayList<>();
        logger = LoggerFactory.getLogger("server");
    }

    public void listen() throws IOException {
        try {
            while (true) {
                Socket user = server.accept();
                logger.info(user.getInetAddress() + " started connecting...");
                users.add(new UserHandler(user, serverData, commandsHandler));
                logger.info(user.getInetAddress() + " successfully connected");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            //TODO change
        } finally {
            server.close();
        }
    }
}
