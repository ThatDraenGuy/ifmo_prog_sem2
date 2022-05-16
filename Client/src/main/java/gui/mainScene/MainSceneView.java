package gui.mainScene;

import collection.meta.CollectibleModel;
import gui.AbstractView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableView;

public class MainSceneView extends AbstractView {
    @FXML
    private Label account;
    @FXML
    private Label server;
    @FXML
    private Label message;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private TableView<CollectibleModel> table;

    private final MainSceneViewModel viewModel = new MainSceneViewModel();

    public void initialize() {
        defaultInitialize(progress.visibleProperty(), message.textProperty(), viewModel);
        account.textProperty().bind(viewModel.getAccount());
        server.textProperty().bind(viewModel.getServer());

    }

    @FXML
    private void logOut(ActionEvent event) {
        viewModel.logOut();
    }

    @FXML
    private void disconnect(ActionEvent event) {
        viewModel.disconnect();
    }

    @FXML
    private void exit(ActionEvent event) {
        viewModel.exit();
    }
}
