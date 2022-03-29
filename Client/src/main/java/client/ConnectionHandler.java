package client;


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
        connect();
    }

    private void connect() {
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

    public Response send(Request data) {
        try {
            Message<Request> message = new Message<>(data);
            out.writeObject(message);
            Message<Response> responseMessage = (Message<Response>) in.readObject();
            return responseMessage.getData();
        } catch (ClassNotFoundException e) {
            consoleHandler.errorMessage(e);
            return null;
            //TODO normal check
        } catch (IOException e) {
            consoleHandler.errorMessage(new ConnectionException("Lost connection to the server, attempting to reconnect..."));
            reconnect();
            return send(data);
        }
    }
}
