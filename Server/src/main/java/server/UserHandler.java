package server;

import message.*;
import org.slf4j.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class UserHandler extends Thread {
    private boolean stopFlag;
    private final ServerHandler myServerHandler;
    //TODO think
    private final Socket user;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Logger logger;
    private final RequestHandler requestHandler;

    public UserHandler(Socket user, RequestHandler requestHandler, Logger logger, ServerHandler serverHandler) throws IOException {
        super(user.getInetAddress().toString());
        this.user = user;
        this.requestHandler = requestHandler;
        this.logger = logger;
        this.myServerHandler = serverHandler;
        this.stopFlag = false;
        in = new ObjectInputStream(user.getInputStream());
        out = new ObjectOutputStream(user.getOutputStream());
        out.flush();
        start();
    }

    @Override
    public void run() {
        while (true) {
            if (stopFlag) return;
            try {
                Message<? extends Request> requestMessage = readMessage();
                Message<? extends Response> responseMessage = handleMessage(requestMessage, requestMessage.getData().getResponseType());
                logger.info("created response");
                out.writeObject(responseMessage);
                logger.info("successfully sent response");
                if (responseMessage.getData().getClass().equals(DisconnectResponse.class)) disconnect();
                //TODO think
            } catch (EOFException e) {
                logger.warn("Lost connection with user");
                disconnect();
            } catch (IOException | ClassNotFoundException e) {
                logger.error(e.toString());
                disconnect();
            }
        }
    }

    public void disconnect() {
        try {
            user.close();
            stopFlag = true;
            myServerHandler.disconnect(this);
            logger.info("Successfully disconnected user");
        } catch (IOException e) {
            logger.error("How did you screw up that badly? " + e);
        }
    }

    public Message<? extends Request> readMessage() throws IOException, ClassNotFoundException {
        return (Message<? extends Request>) in.readObject();
    }

    public <T extends Request, E extends Response> Message<E> handleMessage(Message<T> message, Class<E> responseType) {
        T request = message.getData();
        logger.info("got a " + request.getClass().getSimpleName());
        //TODO think
        return new Message<>(requestHandler.handle(request, responseType));

    }

}
