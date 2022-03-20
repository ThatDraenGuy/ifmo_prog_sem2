package message;

public class DisconnectRequest implements Request {

    @Override
    public Class<DisconnectResponse> getResponseType() {
        return DisconnectResponse.class;
    }
}
