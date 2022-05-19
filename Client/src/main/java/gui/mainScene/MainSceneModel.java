package gui.mainScene;

import app.Controllers;
import collection.CollectionClassesHandler;
import collection.classes.MainCollectible;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import collection.meta.InputtedValue;
import commands.ActionResult;
import commands.CommandArgs;
import commands.Requester;
import exceptions.ValueNotValidException;
import gui.Notifications;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import lombok.Getter;

import java.util.*;

public class MainSceneModel {
    public static final String LOGOUT_TASK_EVENT = "LOGOUT_TASK_EVENT";
    public static final String DISCONNECT_TASK_EVENT = "DISCONNECT_TASK_EVENT";
    public static final String EXIT_TASK_EVENT = "EXIT_TASK_EVENT";
    public static final String DELETE_TASK_EVENT = "DELETE_TASK_EVENT";
    public static final String ADD_TASK_EVENT = "ADD_TASK_EVENT";
    public static final String EDIT_TASK_EVENT = "EDIT_TASK_EVENT";

    private final Requester requester;
    private final CollectionClassesHandler collectionClassesHandler;
    @Getter
    private Optional<ActionResult> logOutResult = Optional.empty();
    @Getter
    private Optional<ActionResult> disconnectResult = Optional.empty();
    @Getter
    private Optional<ActionResult> exitResult = Optional.empty();
    @Getter
    private Optional<ActionResult> deleteResult = Optional.empty();
    @Getter
    private Optional<ActionResult> addResult = Optional.empty();
    @Getter
    private Optional<ActionResult> editResult = Optional.empty();

    public MainSceneModel() {
        requester = Controllers.getRequester();
        collectionClassesHandler = requester.getExecutionController().getTargetClassHandler();
    }


    public void logOut(Void unused) {
        logOutResult = Optional.of(new ActionResult(true, "Successfully logged out"));
        Notifications.publish(LOGOUT_TASK_EVENT);
    }


    public TableView<? extends MainCollectible<?>> getTableView() {
        return collectionClassesHandler.getCurrentCollectionHandler().getTableViewHandler().getTableView();
    }

    public ObservableList<Node> getVisuals() {
        return collectionClassesHandler.getCurrentCollectionHandler().getVisualViewHandler().getImages();
    }

    public ObjectProperty<? extends MainCollectible<?>> getSelectedVisual() {
        return collectionClassesHandler.getCurrentCollectionHandler().getVisualViewHandler().getSelected();
    }

    public CollectibleScheme getScheme() {
        return collectionClassesHandler.getCurrentCollectionHandler().getCollectibleScheme();
    }
}
