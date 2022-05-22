package gui.mainScene;

import collection.classes.MainCollectible;
import gui.AbstractView;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import locales.I18N;


public class MainSceneView extends AbstractView {
    @FXML
    private Label account, accountLabel, server, serverLabel, localeLabel;
    @FXML
    private TextArea message;
    @FXML
    private ProgressIndicator progress;
    private TableView<? extends MainCollectible<?>> tableView;
    @FXML
    private AnchorPane tablePlace;
    @FXML
    private FlowPane visual;
    @FXML
    private Button add, edit, delete, logOut, changeServer, exit;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tableTab, visualTab;
    @FXML
    private GridPane infoGrid;
    @FXML
    private BorderPane mainArea;

    private final MainSceneViewModel viewModel = new MainSceneViewModel();

    @Override
    protected void placeLocaleChoiceBox() {
        infoGrid.add(localeChoiceBox, 1, 2);
    }

    public void initialize() {
        defaultInitialize(progress.visibleProperty(), message.textProperty(), viewModel);
        account.textProperty().bind(viewModel.getAccount());
        server.textProperty().bind(viewModel.getServer());
        viewModel.getTableTabOpened().bind(tableTab.selectedProperty());
        viewModel.getVisualTabOpened().bind(visualTab.selectedProperty());
        viewModel.getTableView().addEventHandler(EventType.ROOT, event -> {
            tableView = viewModel.getTableView();
            tablePlace.getChildren().add(tableView);
            AnchorPane.setTopAnchor(tableView, 0.0);
            AnchorPane.setBottomAnchor(tableView, 0.0);
            AnchorPane.setLeftAnchor(tableView, 0.0);
            AnchorPane.setRightAnchor(tableView, 0.0);
            delete.disableProperty().bind(viewModel.isItemSelected().not());
            edit.disableProperty().bind(viewModel.isItemSelected().not());
            Bindings.bindContent(visual.getChildren(), viewModel.getVisuals());
        });
    }

    @Override
    public void bindLocales() {
        add.textProperty().bind(I18N.getGuiLabelBinding("addButton"));
        edit.textProperty().bind(I18N.getGuiLabelBinding("editButton"));
        delete.textProperty().bind(I18N.getGuiLabelBinding("deleteButton"));
        logOut.textProperty().bind(I18N.getGuiLabelBinding("logOutButton"));
        changeServer.textProperty().bind(I18N.getGuiLabelBinding("changeServerButton"));
        exit.textProperty().bind(I18N.getGuiLabelBinding("exitButton"));
        accountLabel.textProperty().bind(I18N.getGuiLabelBinding("accountLabel"));
        serverLabel.textProperty().bind(I18N.getGuiLabelBinding("serverLabel"));
        localeLabel.textProperty().bind(I18N.getGuiLabelBinding("localeLabel"));
        tableTab.textProperty().bind(I18N.getGuiLabelBinding("tableTab"));
        visualTab.textProperty().bind(I18N.getGuiLabelBinding("visualTab"));
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
