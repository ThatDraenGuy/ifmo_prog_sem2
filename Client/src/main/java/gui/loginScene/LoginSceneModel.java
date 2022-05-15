package gui.loginScene;

import app.Controllers;
import collection.meta.CollectibleModel;
import commands.ActionResult;
import commands.CommandAccessLevel;
import commands.CommandArgs;
import commands.Requester;
import gui.Notifications;
import lombok.Getter;
import security.Account;
import security.AccountFactory;

import java.util.Optional;

public class LoginSceneModel {
    public final static String LOGIN_TASK_EVENT = "LOGIN_TASK_EVENT";
    public final static String REGISTER_TASK_EVENT = "REGISTER_TASK_EVENT";

    private final Requester requester;
    @Getter
    private Optional<ActionResult> loginResult;
    @Getter
    private Optional<ActionResult> registerResult;
    private final AccountFactory accountFactory = new AccountFactory();

    public LoginSceneModel() {
        this.requester = Controllers.getRequester();
    }


    public void login(String name, String password) {
        ActionResult result = requester.request("login", new CommandArgs(createAccount(name, password)));
        loginResult = Optional.ofNullable(result);
        Notifications.publish(LOGIN_TASK_EVENT);
    }

    public void register(String name, String password) {
        ActionResult result = requester.request("register", new CommandArgs(createAccount(name, password)));
        registerResult = Optional.ofNullable(result);
        Notifications.publish(REGISTER_TASK_EVENT);
    }

    private CollectibleModel createAccount(String name, String password) {
        return accountFactory.getModel(new Account(name, password, CommandAccessLevel.GUEST));
    }
}
