package web;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import commands.ServerCommandsHandler;
import lombok.Getter;
import message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import security.Account;
import utility.ListAndId;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class UserHandler extends Thread {
    private final ServerHandler myServerHandler;
    private final ObjectInputStream in;
    private final MessageSender messageSender;
    @Getter
    private final Logger logger;
    private final ServerCommandsHandler commandsHandler;
    private UserData userData;
    private Account userAccount;
    private final SocketChannel userChannel;
    private boolean disconnected;

    public UserHandler(SocketChannel userChannel, ServerCommandsHandler commandsHandler, ServerHandler serverHandler) throws IOException {
        super(userChannel.socket().getInetAddress().toString());
        disconnected = false;
        this.userChannel = userChannel;
        this.commandsHandler = commandsHandler;
        this.logger = LoggerFactory.getLogger("server." + userChannel.getRemoteAddress());
        this.myServerHandler = serverHandler;
        in = new ObjectInputStream(userChannel.socket().getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(userChannel.socket().getOutputStream());
        out.flush();
        messageSender = new MessageSender(out);
        start();
    }

    @Override
    public void run() {
        sendRequest(commandsHandler.formulateFetchServerDataRequest(userAccount));
        while (userChannel.isOpen()) {
            try {
                Message<Request> requestMessage = readMessage();
                Message<Response> responseMessage = handleMessage(requestMessage);
                sendMessage(responseMessage);
                logger.info("successfully sent response");
            } catch (EOFException e) {
                if (!disconnected) logger.warn("Lost connection with user");
                silentDisconnect();
            } catch (IOException | ClassNotFoundException e) {
                if (userChannel.isOpen()) {
                    logger.error(e.toString());
                    disconnect();
                }
            }
        }
        messageSender.setStopped(true);
        messageSender.interrupt();
        logger.info("This thread ended its duty");
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


    public Message<Request> readMessage() throws IOException, ClassNotFoundException {
        @SuppressWarnings({"unchecked"}) Message<Request> requestMessage = (Message<Request>) in.readObject();
        return requestMessage;
    }

    public Message<Response> handleMessage(Message<Request> message) {
        Request request = message.getData();
        Future<Response> responseFuture = myServerHandler.getRequestHandlerService().submit(new RequestHandler(request, this, commandsHandler));
        userData = request.getUserData();
        userAccount = request.getExecutionPayload().getAccount();
        UserDataHandler.handleUserData(this, userData);
        logger.info("got a request from user");
        try {
            Response response = responseFuture.get();
            return new Message<>(response);
        } catch (InterruptedException e) {
            logger.warn("Thread was interrupted while waiting for response, attempting to re-do operation...");
            return handleMessage(message);
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
            return handleMessage(message);
        }

    }

    private void sendRequest(Request request) {
        silentSendRequest(request);
        logger.info("Sent a request to user");
    }

    private void silentSendRequest(Request request) {
        Message<Request> requestMessage = new Message<>(request);
        sendMessage(requestMessage);
    }

    private void sendMessage(Message<?> message) {
        messageSender.sendMessage(message);
    }

    public void sendCollectionChangeRequest(Queue<CollectionChange<? extends MainCollectible<?>>> collectionChanges) {
        silentSendRequest(commandsHandler.formulateCollectionChangeRequest(userData, userAccount, collectionChanges));
        logger.debug("Sent collection changes to user");
    }

    public void sendFullCollectionChangeRequest(ListAndId<? extends MainCollectible<?>> collection) {
        silentSendRequest(commandsHandler.formulateFullCollectionChangeRequest(userData, userAccount, collection));
        logger.info("Sent full collection to user");
    }

    public void sendAccountChangeRequest(Account account) {
        sendRequest(commandsHandler.formulateSetAccountRequest(userData, account));
    }

    public void sendDisconnectRequest() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                silentSendRequest(commandsHandler.formulateDisconnectRequest(userData, userAccount));
                disconnected = true;
                logger.info("Disconnecting the user");
            }
        }, 1000);
    }

}
