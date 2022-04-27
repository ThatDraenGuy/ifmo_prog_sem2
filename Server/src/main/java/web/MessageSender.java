package web;

import lombok.Getter;
import lombok.Setter;
import message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MessageSender extends Thread {
    private final ObjectOutputStream out;
    private final Logger logger;
    private final Queue<Message<?>> messages;
    @Getter
    @Setter
    private boolean stopped;


    public MessageSender(ObjectOutputStream out) {
        this.out = out;
        this.messages = new LinkedList<>();
        this.stopped = false;
        this.logger = LoggerFactory.getLogger("server.messageSender");
        start();
    }


    public void sendMessage(Message<?> message) {
        messages.add(message);
        logger.debug("added new message to queue");
    }


    @Override
    public void run() {
        while (!stopped) {
            try {
                Message<?> message = messages.remove();
                out.writeObject(message);
                logger.debug("Successfully sent message to user");
            } catch (NoSuchElementException ignored) {
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
