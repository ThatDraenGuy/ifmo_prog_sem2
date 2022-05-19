package gui.mainScene;

import collection.classes.MainCollectible;
import gui.AbstractView;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;



public class MainSceneView extends AbstractView {
    @FXML
    private Label account;
    @FXML
    private Label server;
    @FXML
    private Label message;
    @FXML
    private ProgressIndicator progress;
    private TableView<? extends MainCollectible<?>> tableView;
    @FXML
    private AnchorPane tablePlace;
    @FXML
    private FlowPane visual;
    @FXML
    private Button add;
    @FXML
    private Button edit;
    @FXML
    private Button delete;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tableTab;
    @FXML
    private Tab visualTab;

    private final MainSceneViewModel viewModel = new MainSceneViewModel();

    public void initialize() {
        defaultInitialize(progress.visibleProperty(), message.textProperty(), viewModel);
        account.textProperty().bind(viewModel.getAccount());
        server.textProperty().bind(viewModel.getServer());
        viewModel.getTableTabOpened().bind(tableTab.selectedProperty());
        viewModel.getVisualTabOpened().bind(visualTab.selectedProperty());
        viewModel.getTableView().addEventHandler(EventType.ROOT, event -> {
            tableView = viewModel.getTableView();
            tablePlace.getChildren().add(tableView);
            tablePlace.prefWidthProperty().bind(tableView.prefWidthProperty());
            AnchorPane.setTopAnchor(tableView, 0.0);
            AnchorPane.setBottomAnchor(tableView, 0.0);
            AnchorPane.setLeftAnchor(tableView, 0.0);
            AnchorPane.setRightAnchor(tableView, 0.0);
//            BooleanBinding binding = tableView.getSelectionModel().selectedItemProperty().isNull().or(
//                    Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "owner").isNotEqualTo(account.textProperty())
//            );
            delete.disableProperty().bind(viewModel.isItemSelected().not());
            edit.disableProperty().bind(viewModel.isItemSelected().not());
            Bindings.bindContent(visual.getChildren(), viewModel.getVisuals());
        });
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

    @FXML
    private void delete(ActionEvent event) {
        viewModel.delete();
    }

    @FXML
    private void add(ActionEvent event) {
        viewModel.add();
    }

    @FXML
    private void edit(ActionEvent event) {
        viewModel.edit();
    }
}
