package server;

import commands.CommandArgs;
import commands.CommandsHandler;
import commands.ServerCommandsHandler;
import message.*;
import org.slf4j.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;


public class UserHandler extends Thread {
    private boolean stopFlag;
    private boolean disconnectionFlag;
    private final ServerHandler myServerHandler;
    //TODO think
    private final Socket user;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Logger logger;
    private final ServerCommandsHandler commandsHandler;
    private UserData userData;

    public UserHandler(Socket user, ServerCommandsHandler commandsHandler, Logger logger, ServerHandler serverHandler) throws IOException {
        super(user.getInetAddress().toString());
        this.user = user;
        this.commandsHandler = commandsHandler;
        this.logger = logger;
        this.myServerHandler = serverHandler;
        this.stopFlag = false;
        in = new ObjectInputStream(user.getInputStream());
        out = new ObjectOutputStream(user.getOutputStream());
        out.flush();
        start();
    }

    @Override
    public synchronized void run() {
        sendRequest(new CommandRequest(commandsHandler.getFetchServerDataCommand().getData(), new CommandArgs("")));
        while (!stopFlag) {
            try {
//                logger.debug("i'm still here");
                Message<Request> requestMessage = readMessage();
                Message<Response> responseMessage = handleMessage(requestMessage);
                sendMessage(responseMessage);
                logger.info("successfully sent response");
                if (disconnectionFlag) disconnect();
                //TODO think
            } catch (EOFException e) {
                logger.warn("Lost connection with user");
                silentDisconnect();
                return;
            } catch (IOException | ClassNotFoundException e) {
                logger.error(e.toString());
                disconnect();
            }
        }
    }

    private void closeSocket() {
        try {
            stopFlag = true;
            user.close();
        } catch (IOException e) {
            logger.error("How did you screw up that badly? " + e);
        }
    }

    public void silentDisconnect() {
        closeSocket();
        myServerHandler.disconnect(this);
    }

    public void disconnect() {
        silentDisconnect();
        logger.info("Successfully disconnected user");
    }

    public void forceDisconnect() {
        sendRequest(new CommandRequest(userData.getDisconnectCommandData(), new CommandArgs("")));
        closeSocket();
    }

    public Message<Request> readMessage() throws IOException, ClassNotFoundException {
        return (Message<Request>) in.readObject();
    }

    public Message<Response> handleMessage(Message<Request> message) {
        Request request = message.getData();
        userData = request.getUserData();
        if (userData != null) logger.debug(userData + " " + userData.getDisconnectCommandData());
        else logger.debug("bruh");
        disconnectionFlag = request.getCommandData().isDisconnectionNeeded();
        logger.info("got a request");
        return new Message<>(commandsHandler.executeCommand(request));
        //TODO think

    }

    public void sendRequest(Request request) {
        Message<Request> requestMessage = new Message<>(request);
        try {
            sendMessage(requestMessage);
            logger.debug("sent a request");
        } catch (IOException e) {
            logger.error("Couldn't send request to user: " + e.toString());
        }
    }

    private void sendMessage(Message<?> message) throws IOException {
        out.writeObject(message);
    }

}
