package web;


import console.ConsoleHandler;
import exceptions.ConnectionException;
import message.*;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private final String host;
    private final int port;
    private final ConsoleHandler consoleHandler;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ConnectionHandler(String host, int port, ConsoleHandler consoleHandler) throws IOException, ClassNotFoundException {
        this.host = host;
        this.port = port;
        this.consoleHandler = consoleHandler;
//        connect();
    }

    public void connect() {
        consoleHandler.message("Attempting to connect to " + host + ":" + port);
        reconnect();
    }

    private void reconnect() {
        while (true) {
            try {
                Thread.sleep(5000);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if (socket != null && socket.isClosed()) return;
            try {
                socket = new Socket(host, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                consoleHandler.message("Successfully connected to the server");
                return;
            } catch (IOException e) {
                consoleHandler.errorMessage(e);
                consoleHandler.message("Attempting to reconnect...");
            }
        }
    }

    public void disconnect() {
        try {
            socket.close();
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

    public void send(Request data) {
        try {
            Message<Request> message = new Message<>(data);
            consoleHandler.debugMessage("I'm gonna send it");
            sendMessage(message);
            consoleHandler.debugMessage("I sent it.");
        } catch (IOException e) {
            handleLostConnection();
            if (isConnectionOpen()) send(data);
        }
//        try {
//            Message<Request> message = new Message<>(data);
//            sendMessage(message);
//            Message<Response> responseMessage = readMessage();
//            return responseMessage.getData();
//        } catch (ClassNotFoundException e) {
//            consoleHandler.errorMessage(e);
//            return null;
//            //TODO normal check
//        } catch (IOException e) {
//            consoleHandler.errorMessage(new ConnectionException("Lost connection to the server, attempting to reconnect..."));
//            reconnect();
//            return send(data);
//        }
    }

    private void sendMessage(Message<Request> message) throws IOException {
        out.writeObject(message);
    }

    public synchronized <T> Message<T> readMessage() throws IOException, ClassNotFoundException {
        return (Message<T>) in.readObject();
    }

    public boolean isConnectionOpen() {
        return !socket.isClosed() && socket.isConnected();
    }
}
