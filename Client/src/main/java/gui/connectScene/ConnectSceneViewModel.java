package gui.connectScene;

import app.Controllers;
import gui.Notifications;
import javafx.beans.property.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.Getter;

public class ConnectSceneViewModel {
    private final ConnectSceneModel model = new ConnectSceneModel();
    @Getter
    private final StringProperty address;
    @Getter
    private final BooleanProperty success;
    @Getter
    private final StringProperty message;
    @Getter
    private final StringProperty errorMessage;


    public ConnectSceneViewModel() {
        address = new SimpleStringProperty("");
        success = new SimpleBooleanProperty();
        message = new SimpleStringProperty("");
        errorMessage = new SimpleStringProperty("");
        Notifications.subscribe(Notifications.EVENT_MODEL_UPDATE, this, this::update);
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


    private void update(String event) {
        model.getConnectResult().ifPresent((actionResult -> {
            success.setValue(actionResult.isSuccess());
            if (!success.get()) {
                errorMessage.setValue(actionResult.getMessage());
                message.setValue("");
                errorMessage.setValue("");
            } else {
                message.setValue(actionResult.getMessage());
                Controllers.getSceneController().switchToLoginScene();
            }
        }));
    }

    public ReadOnlyDoubleProperty getConnectTaskProgressProperty() {
        return connectTask.progressProperty();
    }

    public ReadOnlyBooleanProperty isConnectTaskRunning() {
        return connectTask.runningProperty();
    }
}
