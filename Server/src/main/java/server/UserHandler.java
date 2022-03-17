package server;

import commands.ServerCommandsHandler;
import message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger logger;

    public UserHandler(Socket user, ServerData serverData, ServerCommandsHandler commandsHandler) throws IOException {
        super(user.getInetAddress().toString());
        this.user = user;
        this.serverData = serverData;
        this.commandsHandler = commandsHandler;
        this.logger = LoggerFactory.getLogger("server." + user.getInetAddress());
        in = new ObjectInputStream(user.getInputStream());
        out = new ObjectOutputStream(user.getOutputStream());
        out.flush();
        out.writeObject(serverData);
        logger.info("successfully sent serverData");
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message<?> message = (Message<?>) in.readObject();
                logger.info("got a message");
                Response response = handleMessage(message);
                out.writeObject(response);
                logger.info("successfully sent response");
            } catch (IOException | ClassNotFoundException e) {
                //TODO
            }
        }
    }

    public Response handleMessage(Message<?> message) {
        Class<?>[] classes = Request.class.getClasses();
        Class<?> dataType = message.getData().getClass();
        for (Class<?> request : classes) {
            try {
                dataType.asSubclass(request);
            } catch (ClassCastException ignored) {
            }
            if (dataType.isAssignableFrom(request)) {

            }
        }
    }
}
