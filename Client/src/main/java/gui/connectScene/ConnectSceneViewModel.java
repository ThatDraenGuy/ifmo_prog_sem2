package gui.connectScene;

import app.Controllers;
import commands.ActionResult;
import gui.AbstractViewModel;
import gui.CommandService;
import gui.Notifications;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.Getter;

public class ConnectSceneViewModel extends AbstractViewModel {
    @Getter
    private final StringProperty address = new SimpleStringProperty("");


    public void connect() {
        connectTask.restart();
    }

    private final Service<Void> connectTask = CommandService.getSimpleArgs("connect", this::connectEvent, address::get);

    private void connectEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
        if (success.get()) Controllers.getSceneController().switchToLoginScene();
    }

    public ReadOnlyDoubleProperty getConnectTaskProgressProperty() {
        return connectTask.progressProperty();
    }

    public BooleanBinding isTaskRunning() {
        return connectTask.runningProperty().isEqualTo(new SimpleBooleanProperty(true));
    }
}
