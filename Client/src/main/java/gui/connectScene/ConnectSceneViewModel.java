package gui.connectScene;

import app.Controllers;
import gui.AbstractViewModel;
import gui.Notifications;
import javafx.beans.property.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.Getter;

public class ConnectSceneViewModel extends AbstractViewModel {
    private final ConnectSceneModel model = new ConnectSceneModel();
    @Getter
    private final StringProperty address = new SimpleStringProperty("");


    public ConnectSceneViewModel() {
        Notifications.subscribe(Notifications.EVENT_MODEL_UPDATE, this, this::connectEvent);
        Notifications.subscribe("connect", this, this::connectEvent);
    }


    public void connect() {
        connectTask.restart();
    }

    private final Service<Void> connectTask = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    updateProgress(0.1, 1.0);
                    updateMessage("Connecting to " + address.get());
                    model.connect(address.get());
                    return null;
                }

                protected void failed() {
                    getException().printStackTrace();
                }
            };
        }
    };


    private void connectEvent(String event) {
        model.getConnectResult().ifPresent((actionResult -> {
            handleActionResult(actionResult);
            if (success.get()) Controllers.getSceneController().switchToLoginScene();
        }));
    }

    public ReadOnlyDoubleProperty getConnectTaskProgressProperty() {
        return connectTask.progressProperty();
    }

    public ReadOnlyBooleanProperty isTaskRunning() {
        return connectTask.runningProperty();
    }
}
