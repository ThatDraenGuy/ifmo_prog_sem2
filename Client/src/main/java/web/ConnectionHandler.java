package web;


import console.ConsoleHandler;
import exceptions.ConnectionException;
import gui.Notifications;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import message.*;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    public static final String DISCONNECT_EVENT = "DISCONNECT_EVENT";
    private String host;
    private int port;
    @Getter
    private final StringProperty address = new SimpleStringProperty();
    private final ConsoleHandler consoleHandler;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ConnectionHandler(ConsoleHandler consoleHandler) throws IOException, ClassNotFoundException {
        this.consoleHandler = consoleHandler;
    }

    public void connect(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        consoleHandler.debugMessage("Attempting to connect to " + host + ":" + port);
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
        address.setValue(host + ":" + port);
    }

    private void reconnect() {
        while (true) {
            try {
                connect(host, port);
                consoleHandler.debugMessage("Successfully reconnected to the server");
                return;
            } catch (IOException e) {
                consoleHandler.errorMessage(e);
            } finally {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public void disconnect() {
        try {
            if (socket != null) socket.close();
            Notifications.publish(DISCONNECT_EVENT);
        } catch (IOException e) {
            consoleHandler.errorMessage(e);
        }
    }

    public void handleLostConnection() {
        consoleHandler.errorMessage(new ConnectionException("Lost connection to the server, attempting to reconnect..."));
        if (!socket.isClosed()) {
            reconnect();
        }
    }

    public void send(Request data) throws ConnectionException {
        if (isConnectionClosed())
            throw new ConnectionException("Sever forcefully ended connection. Reconnection needed");
        try {
            Message<Request> message = new Message<>(data);
            consoleHandler.debugMessage("I'm gonna send it");
            sendMessage(message);
            consoleHandler.debugMessage("I sent it.");
        } catch (IOException e) {
            handleLostConnection();
            if (isConnectionOpen()) send(data);
        }
    }

    private void sendMessage(Message<Request> message) throws IOException {
        out.writeObject(message);
    }

    public synchronized <T> Message<T> readMessage() throws IOException, ClassNotFoundException {
        @SuppressWarnings({"unchecked"})

        Message<T> message = (Message<T>) in.readObject();
        return message;
    }

    public boolean isConnectionOpen() {
        return !socket.isClosed() && socket.isConnected();
    }

    public boolean isConnectionClosed() {
        return socket.isClosed();
    }
}
