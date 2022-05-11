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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageSender extends Thread {
    private final ObjectOutputStream out;
    private final Logger logger;
    private final BlockingQueue<Message<?>> messages;
    @Getter
    @Setter
    private boolean stopped;


    public MessageSender(ObjectOutputStream out) {
        this.out = out;
        this.messages = new ArrayBlockingQueue<>(5);
        this.stopped = false;
        this.logger = LoggerFactory.getLogger("server.messageSender");
        start();
    }


    public void sendMessage(Message<?> message) {
        messages.offer(message);
        logger.debug("added new message to queue");
    }


    @Override
    public void run() {
        while (!stopped) {
            try {
                Message<?> message = messages.take();
                out.writeObject(message);
                logger.debug("Successfully sent message to user");
            } catch (InterruptedException ignored) {
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
