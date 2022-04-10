package web;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import commands.CommandArgs;
import commands.ServerCommandsHandler;
import lombok.Getter;
import message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utility.QueueWithID;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.util.Queue;


public class UserHandler extends Thread {
    private final ServerHandler myServerHandler;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    @Getter
    private final Logger logger;
    private final ServerCommandsHandler commandsHandler;
    private UserData userData;
    //
    private final SocketChannel userChannel;

    public UserHandler(SocketChannel userChannel, ServerCommandsHandler commandsHandler, ServerHandler serverHandler) throws IOException {
        super(userChannel.socket().getInetAddress().toString());
        this.userChannel = userChannel;
        this.commandsHandler = commandsHandler;
        this.logger = LoggerFactory.getLogger("server." + userChannel.getRemoteAddress());
        this.myServerHandler = serverHandler;
        in = new ObjectInputStream(userChannel.socket().getInputStream());
        out = new ObjectOutputStream(userChannel.socket().getOutputStream());
        out.flush();
        start();
    }

    @Override
    public synchronized void run() {
        sendRequest(new CommandRequest(commandsHandler.getFetchServerDataCommand().getData(), new CommandArgs(""), commandsHandler.getServerData()));
        while (userChannel.isOpen()) {
            try {
                Message<Request> requestMessage = readMessage();
                Message<Response> responseMessage = handleMessage(requestMessage);
                sendMessage(responseMessage);
                logger.info("successfully sent response");
            } catch (EOFException e) {
                logger.warn("Lost connection with user");
                silentDisconnect();
                return;
            } catch (IOException | ClassNotFoundException e) {
                if (userChannel.isOpen()) {
                    logger.error(e.toString());
                    disconnect();
                }
            }
        }
    }

    private void closeSocket() {
        try {
            userChannel.close();
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
        @SuppressWarnings({"unchecked"}) Message<Request> requestMessage = (Message<Request>) in.readObject();
        return requestMessage;
    }

    public Message<Response> handleMessage(Message<Request> message) {
        Request request = message.getData();
        userData = request.getUserData();
        UserDataHandler.handleUserData(this, userData);
        logger.info("got a request from user");
        return new Message<>(commandsHandler.executeCommand(request));

    }

    public void sendRequest(Request request) {
        Message<Request> requestMessage = new Message<>(request);
        try {
            sendMessage(requestMessage);
            logger.info("Sent a request to user");
        } catch (IOException e) {
            logger.error("Couldn't send request to user: " + e);
        }
    }

    private void sendMessage(Message<?> message) throws IOException {
        out.writeObject(message);
    }

    public void sendCollectionChangeRequest(Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges) {
        Request request = new CommandRequest(userData.getApplyCollectionChangeCommandData(), new CommandArgs("", collectionChanges));
        sendRequest(request);
    }

    public void sendFullCollectionChangeRequest(QueueWithID<? extends MainCollectible<?>> collection) {
        logger.info("Sent full collection to user");
        Request request = new CommandRequest(userData.getApplyFullCollectionCommandData(), new CommandArgs("", collection));
        sendRequest(request);
    }
}
