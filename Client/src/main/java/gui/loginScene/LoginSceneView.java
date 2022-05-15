package gui.loginScene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginSceneView {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink register;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Label message;

    private final LoginSceneViewModel viewModel = new LoginSceneViewModel();


    public void initialize() {
        viewModel.getUsername().bind(loginField.textProperty());
        viewModel.getPassword().bind(passwordField.textProperty());
        progress.visibleProperty().bind(viewModel.isTaskRunning());
        message.textProperty().bind(viewModel.getMessage());
        viewModel.getErrorMessage().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue != null && !newValue.isEmpty()) {
                        Alert alert = new Alert(
                                Alert.AlertType.ERROR, newValue
                        );
                        alert.showAndWait();
                    }
                }
        );
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
