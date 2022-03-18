package message;

public class ServerDataRequest implements Request {
    public ServerDataRequest() {
    }

    ;

    @Override
    public Class<ServerDataResponse> getResponseType() {
        return ServerDataResponse.class;
    }
}
