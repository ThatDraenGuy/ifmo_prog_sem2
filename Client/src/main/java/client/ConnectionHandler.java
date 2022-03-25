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
        while (true) {
            try {
                socket = new Socket(host, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
                serverData = send(new ServerDataRequest());
                return;
            } catch (IOException e) {
                consoleHandler.errorMessage(e);
            }
        }
    }

    public <T extends Response, E extends Request> T send(E data) {
        try {
            Message<E> message = new Message<>(data);
            out.writeObject(message);
            Message<T> responseMessage = (Message<T>) in.readObject();
            return responseMessage.getData();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
            return null;
            //TODO normal check
        }
    }
}
