package gui.mainScene;

import app.Controllers;
import collection.CollectionClassesHandler;
import collection.classes.MainCollectible;
import commands.ActionResult;
import gui.AbstractViewModel;
import gui.CommandService;
import gui.Notifications;
import gui.Utilities;
import gui.editorDialog.EditorDialog;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import lombok.Getter;
import security.CurrentAccount;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

public class MainSceneViewModel extends AbstractViewModel {
    private final MainSceneModel model = new MainSceneModel();
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
            if (visualTabOpened.get()) return model.getSelectedVisual().get();
            return null;
        }

        @Override
        public MainCollectible<?> get() {
            if (tableTabOpened.get()) return tableView.getSelectionModel().getSelectedItem();
            if (visualTabOpened.get()) return model.getSelectedVisual().get();
            return null;
        }

        @Override
        public String getName() {
            return "selectedItemProperty";
        }
    };

    public MainSceneViewModel() {
        tableView = new TableView<>();
        Notifications.subscribe(MainSceneModel.LOGOUT_TASK_EVENT, this, this::logOutEvent);
        Notifications.subscribe(Notifications.ACCOUNT_CHANGE_EVENT, this, this::accountChangeEvent);
        Notifications.subscribe(CollectionClassesHandler.COLLECTIBLE_SCHEME_CHANGE_EVENT, this, this::collectibleSchemeChangeEvent);
    }

    public void logOut() {
        logOutTask.restart();
    }

    private final Service<Void> logOutTask = Utilities.getDefaultService(model::logOut);

    private void logOutEvent(String event) {
        model.getLogOutResult().ifPresent(actionResult -> {
            handleActionResult(actionResult);
            if (success.get()) Controllers.getSceneController().switchToLoginScene();
        });
    }

    public void disconnect() {
        disconnectTask.restart();
    }

    private final Service<Void> disconnectTask = CommandService.getNoArgs("disconnect", this::disconnectEvent);

    private void disconnectEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
        if (success.get()) Controllers.getSceneController().switchToConnectScene();
    }

    public void exit() {
        exitTask.restart();
    }

    private final Service<Void> exitTask = CommandService.getNoArgs("exit", this::handleActionResult);

    public void delete() {
        deleteTask.restart();
    }

    private final Service<Void> deleteTask = CommandService.getSimpleArgs("remove_by_id", this::deleteEvent, () ->
            tableView.getSelectionModel().getSelectedItem().getId().toString());


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
        tableView = model.getTableView();
        visuals = model.getVisuals();
        addDialog = new EditorDialog(model.getScheme(), new ButtonType("Add", ButtonBar.ButtonData.APPLY));
        editDialog = new EditorDialog(model.getScheme(), new ButtonType("Edit", ButtonBar.ButtonData.APPLY));
        oldTableView.fireEvent(new Event(EventType.ROOT));
    }

    @Override
    public BooleanBinding isTaskRunning() {
        return Stream.of(deleteTask.runningProperty(), exitTask.runningProperty(), disconnectTask.runningProperty(), logOutTask.runningProperty()).
                map(property -> property.isEqualTo(TRUE)).reduce(Bindings::or).get();
    }

    public BooleanBinding isItemSelected() {
        BooleanBinding tableBind = tableTabOpened.isEqualTo(TRUE).and(tableView.getSelectionModel().selectedItemProperty().isNotNull()).and(
                Bindings.select(tableView.getSelectionModel().selectedItemProperty(), "owner").isEqualTo(account));
        BooleanBinding visualBind = visualTabOpened.isEqualTo(TRUE).and(model.getSelectedVisual().isNotNull()).and(
                Bindings.select(model.getSelectedVisual(), "owner").isEqualTo(account));
        return Bindings.or(tableBind, visualBind);
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
