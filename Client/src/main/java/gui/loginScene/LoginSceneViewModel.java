package gui.loginScene;

import app.Controllers;
import gui.AbstractViewModel;
import gui.Notifications;
import javafx.beans.property.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.Getter;

public class LoginSceneViewModel extends AbstractViewModel {
    private final LoginSceneModel model = new LoginSceneModel();
    @Getter
    private final StringProperty username = new SimpleStringProperty("");
    @Getter
    private final StringProperty password = new SimpleStringProperty("");


    public LoginSceneViewModel() {
        Notifications.subscribe(LoginSceneModel.LOGIN_TASK_EVENT, this, this::loginEvent);
        Notifications.subscribe(LoginSceneModel.REGISTER_TASK_EVENT, this, this::registerEvent);
    }

    public void login() {
        loginTask.restart();
    }

    private final Service<Void> loginTask = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    updateProgress(0.1, 1.0);
                    model.login(username.get(), password.get());
                    return null;
                }
            };
        }
    };

    public void register() {
        registerTask.restart();
    }

    private final Service<Void> registerTask = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    updateProgress(0.1, 1.0);
                    model.register(username.get(), password.get());
                    return null;
                }
            };
        }
    };

    private void loginEvent(String event) {
        model.getLoginResult().ifPresent((actionResult -> {
            handleActionResult(actionResult);
            if (success.get()) Controllers.getSceneController().switchToMainScene();
        }));
    }

    private void registerEvent(String event) {
        model.getRegisterResult().ifPresent((this::handleActionResult));
    }

    public ReadOnlyBooleanProperty isTaskRunning() {
        SimpleBooleanProperty property = new SimpleBooleanProperty();
        property.set(loginTask.isRunning() || registerTask.isRunning());
        return property;
    }
}
