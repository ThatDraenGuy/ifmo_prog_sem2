package gui.connectScene;

import app.Controllers;
import commands.ActionResult;
import commands.CommandArgs;
import commands.Requester;
import gui.Notifications;
import lombok.Getter;

import java.util.Optional;

public class ConnectSceneModel {
    @Getter
    private Optional<ActionResult> connectResult;

    private final Requester requester;

    public ConnectSceneModel() {
        this.requester = Controllers.getRequester();
    }


    public void connect(String address) {
        ActionResult result = requester.request("connect", new CommandArgs(address));
        connectResult = Optional.ofNullable(result);
        Notifications.publish(Notifications.EVENT_MODEL_UPDATE);
    }
}
