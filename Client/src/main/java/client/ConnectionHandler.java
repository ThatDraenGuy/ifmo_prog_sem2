package client;


import lombok.Getter;
import message.*;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    @Getter
    private ServerDataResponse serverData;

    public ConnectionHandler(String host, int port) throws IOException, ClassNotFoundException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
        serverData = send(new ServerDataRequest());
        System.out.println(serverData.getTargetClass());
    }

    public Response sendRequest(Request request) {
        try {
            out.writeObject(request);
            return (Response) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);
            return null;
            //TODO normal check
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
