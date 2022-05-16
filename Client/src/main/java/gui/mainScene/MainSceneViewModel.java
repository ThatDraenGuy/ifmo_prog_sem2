package gui.mainScene;

import app.Controllers;
import collection.CollectionClassesHandler;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import gui.AbstractViewModel;
import gui.Notifications;
import gui.Utilities;
import gui.ViewModel;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.Getter;
import security.CurrentAccount;

public class MainSceneViewModel extends AbstractViewModel {
    private final MainSceneModel model = new MainSceneModel();
    @Getter
    private final StringProperty account = new SimpleStringProperty("");
    @Getter
    private final StringProperty server = new SimpleStringProperty("");

    public MainSceneViewModel() {
        Notifications.subscribe(MainSceneModel.LOGOUT_TASK_EVENT, this, this::logOutEvent);
        Notifications.subscribe(MainSceneModel.DISCONNECT_TASK_EVENT, this, this::disconnectEvent);
        Notifications.subscribe(Notifications.ACCOUNT_CHANGE_EVENT, this, this::accountChangeEvent);
        Notifications.subscribe(CollectionClassesHandler.COLLECTIBLE_SCHEME_CHANGE_EVENT, this, this::collectibleSchemeChangeEvent);
    }

    public void logOut() {
        logOutTask.restart();
    }

    private final Service<Void> logOutTask = Utilities.getDefaultService(model::logOut);

    private void logOutEvent(String event) {
        model.getLogOutResult().ifPresent(actionResult -> {
            handleActionResult(actionResult);
            if (success.get()) Controllers.getSceneController().switchToLoginScene();
        });
    }

    public void disconnect() {
        disconnectTask.restart();
    }

    private final Service<Void> disconnectTask = Utilities.getDefaultService(model::disconnect);

    private void disconnectEvent(String event) {
        model.getDisconnectResult().ifPresent(actionResult -> {
            handleActionResult(actionResult);
            if (success.get()) Controllers.getSceneController().switchToConnectScene();
        });
    }

    public void exit() {
        exitTask.restart();
    }

    private final Service<Void> exitTask = Utilities.getDefaultService(model::exit);

    private void accountChangeEvent(String event) {
        account.setValue(CurrentAccount.getAccount().getName());
    }

    private void collectibleSchemeChangeEvent(String event) {
        CollectibleScheme scheme = model.getCollectibleScheme();
        for (String fieldName : scheme.getFieldsData().keySet()) {
            FieldData data = scheme.getFieldsData().get(fieldName);
        }
    }

    @Override
    public ReadOnlyBooleanProperty isTaskRunning() {
        return new SimpleBooleanProperty();
        //TODO!!!
    }
}
