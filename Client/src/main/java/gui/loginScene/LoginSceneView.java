package gui.loginScene;

import gui.AbstractView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import locales.I18N;

public class LoginSceneView extends AbstractView {
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginLabel, passwordLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink register;
    @FXML
    protected ProgressIndicator progress;
    @FXML
    protected Label message;
    @FXML
    private AnchorPane localeBoxPlace;

    private final LoginSceneViewModel viewModel = new LoginSceneViewModel();


    @Override
    protected void placeLocaleChoiceBox() {
        localeBoxPlace.getChildren().add(localeChoiceBox);
        AnchorPane.setRightAnchor(localeChoiceBox, 0d);
        AnchorPane.setLeftAnchor(localeChoiceBox, 0d);
        AnchorPane.setTopAnchor(localeChoiceBox, 0d);
        AnchorPane.setBottomAnchor(localeChoiceBox, 0d);
    }

    public void initialize() {
        defaultInitialize(progress.visibleProperty(), message.textProperty(), viewModel);
        viewModel.getUsername().bind(loginField.textProperty());
        viewModel.getPassword().bind(passwordField.textProperty());
    }

    @Override
    public void bindLocales() {
        loginLabel.textProperty().bind(I18N.getGuiLabelBinding("loginLabel"));
        passwordLabel.textProperty().bind(I18N.getGuiLabelBinding("passwordLabel"));
        loginField.promptTextProperty().bind(I18N.getGuiLabelBinding("loginPrompt"));
        passwordField.promptTextProperty().bind(I18N.getGuiLabelBinding("passwordPrompt"));
        loginButton.textProperty().bind(I18N.getGuiLabelBinding("loginButton"));
        register.textProperty().bind(I18N.getGuiLabelBinding("registerButton"));
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
