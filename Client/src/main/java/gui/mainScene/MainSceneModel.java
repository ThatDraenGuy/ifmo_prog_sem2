package gui.mainScene;

import app.Controllers;
import collection.CollectionClassesHandler;
import collection.meta.CollectibleScheme;
import commands.ActionResult;
import commands.CommandArgs;
import commands.Requester;
import gui.Notifications;
import lombok.Getter;

import java.util.Optional;

public class MainSceneModel {
    public static final String LOGOUT_TASK_EVENT = "LOGOUT_TASK_EVENT";
    public static final String DISCONNECT_TASK_EVENT = "DISCONNECT_TASK_EVENT";
    public static final String EXIT_TASK_EVENT = "EXIT_TASK_EVENT";

    private final Requester requester;
    private final CollectionClassesHandler collectionClassesHandler;
    @Getter
    private Optional<ActionResult> logOutResult;
    @Getter
    private Optional<ActionResult> disconnectResult;
    @Getter
    private Optional<ActionResult> exitResult;


    public MainSceneModel() {
        requester = Controllers.getRequester();
        collectionClassesHandler = requester.getExecutionController().getTargetClassHandler();
    }


    public void logOut(Void unused) {
        logOutResult = Optional.of(new ActionResult(true, "Successfully logged out"));
        Notifications.publish(LOGOUT_TASK_EVENT);
    }

    public void disconnect(Void unused) {
        ActionResult result = requester.request("disconnect", new CommandArgs());
        disconnectResult = Optional.ofNullable(result);
        Notifications.publish(DISCONNECT_TASK_EVENT);
    }

    public void exit(Void unused) {
        requester.request("exit", new CommandArgs());
    }


    public CollectibleScheme getCollectibleScheme() {
        return collectionClassesHandler.getCurrentCollectionHandler().getCollectibleScheme();
    }
}
