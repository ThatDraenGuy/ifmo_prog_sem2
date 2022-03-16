package server;

import commands.ServerCommandsHandler;
import message.Request;
import message.Response;
import message.ServerData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserHandler extends Thread {
    private Socket user;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerData serverData;
    private ServerCommandsHandler commandsHandler;

    public UserHandler(Socket user, ServerData serverData, ServerCommandsHandler commandsHandler) throws IOException {
        this.user = user;
        this.serverData = serverData;
        this.commandsHandler = commandsHandler;
        in = new ObjectInputStream(user.getInputStream());
        out = new ObjectOutputStream(user.getOutputStream());
        out.writeObject(serverData);
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Request request = (Request) in.readObject();
                Response response = commandsHandler.executeCommand(request);
                out.writeObject(response);
            } catch (IOException | ClassNotFoundException e) {
                //TODO
            }
        }
    }
}
