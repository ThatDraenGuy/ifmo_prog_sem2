package gui.connectScene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class ConnectSceneView {
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
        viewModel.getAddress().bind(address.textProperty());
        message.textProperty().bind(viewModel.getMessage());
//        progress.progressProperty().bind(viewModel.getConnectTaskProgressProperty());
        progress.visibleProperty().bind(viewModel.isConnectTaskRunning());
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
    private void connect(ActionEvent event) {
        viewModel.connect();
    }
}
