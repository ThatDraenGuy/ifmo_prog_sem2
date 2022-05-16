package gui.connectScene;

import gui.AbstractView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class ConnectSceneView extends AbstractView {
    @FXML
    private TextField address;
    @FXML
    private Label infoLabel;
    @FXML
    private Label message;
    @FXML
    private ProgressIndicator progress;

    private final ConnectSceneViewModel viewModel = new ConnectSceneViewModel();


    public void initialize() {
        defaultInitialize(progress.visibleProperty(), message.textProperty(), viewModel);
        viewModel.getAddress().bind(address.textProperty());
    }

    @FXML
    private void connect(ActionEvent event) {
        viewModel.connect();
    }
}
