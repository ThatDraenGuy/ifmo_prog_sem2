package client;


import lombok.Getter;
import message.Request;
import message.Response;
import message.ServerData;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    @Getter
    private ServerData serverData;

    public ConnectionHandler(String host, int port) throws IOException, ClassNotFoundException {
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        serverData = (ServerData) in.readObject();
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
}
