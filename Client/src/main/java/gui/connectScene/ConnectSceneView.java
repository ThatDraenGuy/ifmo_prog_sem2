package gui.connectScene;

import gui.AbstractView;
import javafx.scene.layout.AnchorPane;
import locales.I18N;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class ConnectSceneView extends AbstractView {
    @FXML
    private Label connectLabel;
    @FXML
    private TextField address;
    @FXML
    private Button connectButton;
    @FXML
    private Label message;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private AnchorPane localeBoxPlace;

    private final ConnectSceneViewModel viewModel = new ConnectSceneViewModel();


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
        viewModel.getAddress().bind(address.textProperty());
    }

    @Override
    public void bindLocales() {
        address.promptTextProperty().bind(I18N.getGuiLabelBinding("connectPrompt"));
        connectButton.textProperty().bind(I18N.getGuiLabelBinding("connectButton"));
        connectLabel.textProperty().bind(I18N.getGuiLabelBinding("connectLabel"));
    }

    @FXML
    private void connect(ActionEvent event) {
        viewModel.connect();
    }
}
