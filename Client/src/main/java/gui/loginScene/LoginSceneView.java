package gui.loginScene;

import commands.AbstractCommand;
import gui.AbstractView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginSceneView extends AbstractView {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink register;
    @FXML
    protected ProgressIndicator progress;
    @FXML
    protected Label message;

    private final LoginSceneViewModel viewModel = new LoginSceneViewModel();


    public void initialize() {
        defaultInitialize(progress.visibleProperty(), message.textProperty(), viewModel);
        viewModel.getUsername().bind(loginField.textProperty());
        viewModel.getPassword().bind(passwordField.textProperty());
    }


    @FXML
    private void login(ActionEvent event) {
        viewModel.login();
    }

    @FXML
    private void register(ActionEvent event) {
        viewModel.register();
    }
}
