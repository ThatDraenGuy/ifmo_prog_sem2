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
import javafx.scene.control.TableView;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public void disconnect(Void unused) {
        ActionResult result = requester.request("disconnect", new CommandArgs());
        disconnectResult = Optional.ofNullable(result);
        Notifications.publish(DISCONNECT_TASK_EVENT);
    }

    public void exit(Void unused) {
        requester.request("exit", new CommandArgs());
    }

    public void delete(long id) {
        ActionResult result = requester.request("remove_by_id", new CommandArgs(String.valueOf(id)));
        deleteResult = Optional.ofNullable(result);
        Notifications.publish(DELETE_TASK_EVENT);
    }

    public void add(Map<String, String> map) {
        try {
            ActionResult result = requester.request("add", new CommandArgs(build(map)));
            addResult = Optional.ofNullable(result);
        } catch (ValueNotValidException e) {
            addResult = Optional.of(new ActionResult(false, "invalid data"));
        } finally {
            Notifications.publish(ADD_TASK_EVENT);
        }
    }

    public void edit(long id, Map<String, String> map) {
        try {
            ActionResult result = requester.request("update", new CommandArgs(String.valueOf(id), build(map)));
            editResult = Optional.ofNullable(result);
        } catch (ValueNotValidException e) {
            editResult = Optional.of(new ActionResult(false, "invalid data"));
        } finally {
            Notifications.publish(EDIT_TASK_EVENT);
        }
    }

    public TableView<? extends MainCollectible<?>> getTableView() {
        return collectionClassesHandler.getCurrentTableViewHandler().getTableView();
    }

    public CollectibleScheme getScheme() {
        return collectionClassesHandler.getCurrentCollectionHandler().getCollectibleScheme();
    }

    private CollectibleModel build(Map<String, String> map) throws ValueNotValidException {
        CollectibleScheme scheme = getScheme();
        return new CollectibleModel(scheme, getMap(scheme, map));
    }

    private Map<String, InputtedValue> getMap(CollectibleScheme collectibleScheme, Map<String, String> stringMap) {
        Map<String, InputtedValue> map = new HashMap<>();
        for (String key : collectibleScheme.getFieldsData().keySet()) {
            FieldData fieldData = collectibleScheme.getFieldsData().get(key);
            if (fieldData.isUserWritable()) {
                if (!fieldData.isCollectible()) map.put(key, new InputtedValue(stringMap.get(key)));
                else map.put(key, new InputtedValue(getMap(fieldData.getCollectibleScheme(), stringMap)));
            }
        }
        return map;
    }
}
