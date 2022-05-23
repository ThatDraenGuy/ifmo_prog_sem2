package gui.mainScene;

import app.Controllers;
import collection.CollectionClassesHandler;
import collection.TableViewHandler;
import collection.VisualViewHandler;
import collection.classes.MainCollectible;
import collection.meta.CollectibleScheme;
import commands.ActionResult;
import gui.AbstractViewModel;
import gui.CommandService;
import gui.Notifications;
import gui.Utilities;
import gui.editorDialog.EditorDialog;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import lombok.Getter;
import security.CurrentAccount;
import web.ConnectionHandler;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class MainSceneViewModel extends AbstractViewModel {
    @Getter
    private VisualViewHandler<?> visualViewHandler;
    @Getter
    private TableViewHandler<?> tableViewHandler;
    @Getter
    private final StringProperty account = new SimpleStringProperty("");
    @Getter
    private final StringProperty server = new SimpleStringProperty("");
    @Getter
    private TableView<? extends MainCollectible<?>> tableView;
    @Getter
    private ObservableList<Node> visuals;
    @Getter
    private EditorDialog addDialog;
    @Getter
    private EditorDialog editDialog;
    @Getter
    private Map<String, String> editorDialogAnswer;
    @Getter
    private final BooleanProperty tableTabOpened = new SimpleBooleanProperty();
    @Getter
    private final BooleanProperty visualTabOpened = new SimpleBooleanProperty();
    private final ObjectProperty<? extends MainCollectible<?>> selectedItemProperty = new ObjectPropertyBase<MainCollectible<?>>() {
        @Override
        public Object getBean() {
            if (tableTabOpened.get()) return tableView.getSelectionModel().getSelectedItem();
            if (visualTabOpened.get()) return visualViewHandler.getSelected().get();
            return null;
        }

        @Override
        public MainCollectible<?> get() {
            if (tableTabOpened.get()) return tableView.getSelectionModel().getSelectedItem();
            if (visualTabOpened.get()) return visualViewHandler.getSelected().get();
            return null;
        }

        @Override
        public String getName() {
            return "selectedItemProperty";
        }
    };

    public MainSceneViewModel() {
        tableView = new TableView<>();
        Notifications.subscribe(Notifications.ACCOUNT_CHANGE_EVENT, this, this::accountChangeEvent);
        Notifications.subscribe(CollectionClassesHandler.COLLECTIBLE_SCHEME_CHANGE_EVENT, this, this::collectibleSchemeChangeEvent);
        Notifications.subscribe(ConnectionHandler.DISCONNECT_EVENT, this, this::handleDisconnection);
        visualTabOpened.addListener(((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && newValue.equals(true)) {
                visualViewHandler.playAnimation();
            }
        }));
    }

    public void logOut() {
        Controllers.getSceneController().switchToLoginScene();
    }

    public void disconnect() {
        disconnectTask.restart();
    }

    private final Service<Void> disconnectTask = CommandService.getNoArgs("disconnect", this::disconnectEvent);

    private void disconnectEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
        if (success.getValue()) Controllers.getSceneController().switchToConnectScene();
    }

    private void handleDisconnection(String event) {
        Controllers.getSceneController().switchToConnectScene();
    }

    public void exit() {
        exitTask.restart();
    }

    private final Service<Void> exitTask = CommandService.getNoArgs("exit", this::handleActionResult);

    public void delete() {
        deleteTask.restart();
    }

    private final Service<Void> deleteTask = CommandService.getSimpleArgs("remove_by_id", this::deleteEvent, () ->
            selectedItemProperty.getValue().getId().toString());


    private void deleteEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
    }

    public void add() {
        Optional<Map<String, String>> mapOptional = addDialog.getDialog().showAndWait();
        if (mapOptional.isEmpty()) return;
        editorDialogAnswer = mapOptional.get();
        addTask.restart();
    }

    private final Service<Void> addTask = CommandService.getComplexArgs("add", this::addEvent, this::getEditorDialogAnswer);

    private void addEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
    }

//    private Map<String,String> promptEditorDialog() {
//        var ref = new Object() {
//            Map<String, String> res;
//        };
//        Platform.runLater(() -> {
//            Optional<Map<String, String>> mapOptional = addDialog.getDialog().showAndWait();
//            if (mapOptional.isEmpty()) return;
//            ref.res = mapOptional.get();
//        });
//        return ref.res;
//    }

    public void edit() {
        editDialog.setValues(selectedItemProperty.getValue().toModel());
        Optional<Map<String, String>> mapOptional = editDialog.getDialog().showAndWait();
        if (mapOptional.isEmpty()) return;
        editorDialogAnswer = mapOptional.get();
        editTask.restart();
    }

    private final Service<Void> editTask = CommandService.getBothArgs("update", this::editEvent, () ->
            selectedItemProperty.getValue().getId().toString(), this::getEditorDialogAnswer);

    private void editEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
    }

    private void accountChangeEvent(String event) {
        account.setValue(CurrentAccount.getAccount().getName());
    }

    private void collectibleSchemeChangeEvent(String event) {
        TableView<?> oldTableView = tableView;
        visualViewHandler = new VisualViewHandler<>(Controllers.getCollectionClassesHandler().getCurrentCollectionHandler());
        tableViewHandler = new TableViewHandler<>(Controllers.getCollectionClassesHandler().getCurrentCollectionHandler());
        tableView = tableViewHandler.getTableView();
        visuals = visualViewHandler.getImages();
        addDialog = new EditorDialog(getScheme(), new ButtonType("addButton", ButtonBar.ButtonData.APPLY));
        editDialog = new EditorDialog(getScheme(), new ButtonType("editButton", ButtonBar.ButtonData.APPLY));
        oldTableView.fireEvent(new Event(EventType.ROOT));
    }

    @Override
    public BooleanBinding isTaskRunning() {
        return Stream.of(deleteTask.runningProperty(), exitTask.runningProperty(), disconnectTask.runningProperty()).
                map(property -> property.isEqualTo(TRUE)).reduce(Bindings::or).get();
    }

    public BooleanBinding isItemSelected() {
        BooleanBinding tableBind = tableTabOpened.isEqualTo(TRUE).and(tableView.getSelectionModel().selectedItemProperty().isNotNull()).and(
                Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "owner").isEqualTo(account));
        BooleanBinding visualBind = visualTabOpened.isEqualTo(TRUE).and(visualViewHandler.getSelected().isNotNull()).and(
                Bindings.select(visualViewHandler.getSelected(), "owner").isEqualTo(account));
        return Bindings.or(tableBind, visualBind);
    }

    public CollectibleScheme getScheme() {
        return Controllers.getCollectionClassesHandler().getCurrentCollectionHandler().getCollectibleScheme();
    }

//    public ObjectProperty<? extends MainCollectible<?>> selectedItemProperty() {
//        return new ObjectPropertyBase<MainCollectible<?>>() {
//            @Override
//            public Object getBean() {
//                if (tableTabOpened.get()) return tableView.getSelectionModel().getSelectedItem();
//                if (visualTabOpened.get()) return model.getSelectedVisual();
//                return null;
//            }
//
//            @Override
//            public String getName() {
//                return "selectedItemProperty";
//            }
//        };
//    }
}
