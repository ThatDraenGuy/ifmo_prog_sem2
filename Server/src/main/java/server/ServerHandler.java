package server;

import commands.ServerCommandsHandler;
import message.ServerData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandler {
    private ArrayList<UserHandler> users;
    private ServerCommandsHandler commandsHandler;
    private ServerData serverData;
    private ServerSocket server;
    final private int port = 2525;

    public ServerHandler(ServerCommandsHandler commandsHandler, ServerData serverData) throws IOException {
        server = new ServerSocket(port);
        this.commandsHandler = commandsHandler;
        this.serverData = serverData;
    }

    public void listen() throws IOException {
        try {
            while (true) {
                Socket user = server.accept();
                users.add(new UserHandler(user, serverData, commandsHandler));
            }
        } finally {
            server.close();
        }
    }
}
