package gui.loginScene;

import app.Controllers;
import commands.ActionResult;
import gui.AbstractViewModel;
import gui.CommandService;
import gui.Notifications;
import javafx.beans.property.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.Getter;

public class LoginSceneViewModel extends AbstractViewModel {
    @Getter
    private final StringProperty username = new SimpleStringProperty("");
    @Getter
    private final StringProperty password = new SimpleStringProperty("");


    public void login() {
        loginTask.restart();
    }

    private final Service<Void> loginTask = CommandService.getAccountArgs("login", this::loginEvent, username::get, password::get);

    public void register() {
        registerTask.restart();
    }

    private final Service<Void> registerTask = CommandService.getAccountArgs("register", this::registerEvent, username::get, password::get);

    private void loginEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
        if (success.get()) Controllers.getSceneController().switchToMainScene();
    }

    private void registerEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
    }

    public ReadOnlyBooleanProperty isTaskRunning() {
        SimpleBooleanProperty property = new SimpleBooleanProperty();
        property.set(loginTask.isRunning() || registerTask.isRunning());
        return property;
    }
}
