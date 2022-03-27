package client;


import console.ConsoleHandler;
import exceptions.ConnectionException;
import lombok.Getter;
import message.*;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectionHandler {
    private final String host;
    private final int port;
    private final ConsoleHandler consoleHandler;
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    @Getter
    private ServerDataResponse serverData;

    public ConnectionHandler(String host, int port, ConsoleHandler consoleHandler) throws IOException, ClassNotFoundException {
        this.host = host;
        this.port = port;
        this.consoleHandler = consoleHandler;
        connect();
    }

    private void connect() {
        consoleHandler.message("Attempting to connect to connect to " + host + ":" + port);
        reconnect();
    }

    private void reconnect() {
        while (true) {
            try {
                socket = new Socket(host, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                serverData = send(new ServerDataRequest());
                consoleHandler.message("Successfully connected to the server");
                return;
            } catch (IOException e) {
                consoleHandler.errorMessage(e);
                try {
                    Thread.sleep(5000);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                consoleHandler.message("Attempting to reconnect...");
            }
        }
    }

    public <T extends Response, E extends Request> T send(E data) {
        try {
            Message<E> message = new Message<>(data);
            out.writeObject(message);
            Message<T> responseMessage = (Message<T>) in.readObject();
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
