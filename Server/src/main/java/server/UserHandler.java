package server;

import message.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class UserHandler extends Thread {
    private Socket user;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Logger logger;
    private final RequestHandler requestHandler;

    public UserHandler(Socket user, RequestHandler requestHandler, Logger logger) throws IOException {
        super(user.getInetAddress().toString());
        this.user = user;
        this.requestHandler = requestHandler;
        this.logger = logger;
        in = new ObjectInputStream(user.getInputStream());
        out = new ObjectOutputStream(user.getOutputStream());
        out.flush();
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message<? extends Request> requestMessage = readMessage();
                Message<? extends Response> responseMessage = handleMessage(requestMessage, requestMessage.getData().getResponseType());
                logger.info("created response");
                out.writeObject(responseMessage);
                logger.info("successfully sent response");
            } catch (IOException | ClassNotFoundException e) {
                logger.error(e.toString());
                //TODO
            }
        }
    }

    public Message<? extends Request> readMessage() throws IOException, ClassNotFoundException {
        return (Message<? extends Request>) in.readObject();
    }

    public <T extends Request, E extends Response> Message<E> handleMessage(Message<T> message, Class<E> responseType) {
        T request = message.getData();
        logger.info("got a " + request.getClass().getSimpleName());
        //TODO
        return new Message<>(requestHandler.handle(request, responseType));

    }

}
