package server;

import commands.CommandsHandler;
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
    private final CommandsHandler commandsHandler;

    public UserHandler(Socket user, CommandsHandler commandsHandler, Logger logger, ServerHandler serverHandler) throws IOException {
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
        while (!stopFlag) {
            try {
//                logger.debug("i'm still here");
                Message<Request> requestMessage = readMessage();
                Message<Response> responseMessage = handleMessage(requestMessage);
                logger.info("created response");
                out.writeObject(responseMessage);
                logger.info("successfully sent response");
                if (disconnectionFlag) disconnect();
                //TODO think
            } catch (EOFException e) {
                logger.warn("Lost connection with user");
                silentDisconnect();
            } catch (IOException | ClassNotFoundException e) {
                logger.error(e.toString());
                disconnect();
            }
        }
    }

    public void silentDisconnect() {
        try {
            user.close();
            stopFlag = true;
            myServerHandler.disconnect(this);
//            this.interrupt();
        } catch (IOException e) {
            logger.error("How did you screw up that badly? " + e);
        }
    }

    public void disconnect() {
        silentDisconnect();
        logger.info("Successfully disconnected user");
    }

    public Message<Request> readMessage() throws IOException, ClassNotFoundException {
        return (Message<Request>) in.readObject();
    }

    public Message<Response> handleMessage(Message<Request> message) {
        Request request = message.getData();
        disconnectionFlag = request.getCommandData().isDisconnectionNeeded();
        logger.info("got a request");
        return new Message<>(commandsHandler.executeCommand(request));
        //TODO think

    }

}
