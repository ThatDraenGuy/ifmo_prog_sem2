package web;


import console.ConsoleHandler;
import exceptions.ConnectionException;
import message.*;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private String host;
    private int port;
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
        consoleHandler.message("Attempting to connect to " + host + ":" + port);
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
    }

    private void reconnect() {
        while (true) {
            try {
                connect(host, port);
                consoleHandler.message("Successfully reconnected to the server");
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
