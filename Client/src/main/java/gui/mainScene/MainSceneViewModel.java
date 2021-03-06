package gui.mainScene;

import app.Controllers;
import collection.CollectionClassesHandler;
import collection.ObservableCollection;
import collection.TableViewHandler;
import collection.VisualViewHandler;
import collection.classes.MainCollectible;
import collection.meta.CollectibleScheme;
import commands.ActionResult;
import gui.AbstractViewModel;
import gui.CommandService;
import gui.Notifications;
import gui.dialogs.EditorDialog;
import gui.dialogs.FilterDialog;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
    private ObservableCollection<?> observableCollection;
    @Getter
    private final StringProperty account = new SimpleStringProperty("");
    @Getter
    private final StringProperty server = new SimpleStringProperty("");
    @Getter
    private final ObjectProperty<TableView<? extends MainCollectible<?>>> tableView;
    @Getter
    private Node visuals;
    @Getter
    private EditorDialog addDialog;
    @Getter
    private EditorDialog editDialog;
    @Getter
    private FilterDialog filterDialog;
    @Getter
    private Map<String, String> dialogAnswer;
    @Getter
    private final BooleanProperty tableTabOpened = new SimpleBooleanProperty();
    @Getter
    private final BooleanProperty visualTabOpened = new SimpleBooleanProperty();
    private final ObjectProperty<? extends MainCollectible<?>> selectedItemProperty = new ObjectPropertyBase<>() {
        @Override
        public Object getBean() {
            if (tableTabOpened.get()) return tableView.getValue().getSelectionModel().getSelectedItem();
            if (visualTabOpened.get()) return visualViewHandler.getSelected().get();
            return null;
        }

        @Override
        public MainCollectible<?> get() {
            if (tableTabOpened.get()) return tableView.getValue().getSelectionModel().getSelectedItem();
            if (visualTabOpened.get()) return visualViewHandler.getSelected().get();
            return null;
        }

        @Override
        public String getName() {
            return "selectedItemProperty";
        }
    };

    public MainSceneViewModel() {
        tableView = new SimpleObjectProperty<>();
        Notifications.subscribe(Notifications.ACCOUNT_CHANGE_EVENT, this, this::accountChangeEvent);
        Notifications.subscribe(CollectionClassesHandler.COLLECTIBLE_SCHEME_CHANGE_EVENT, this, this::collectibleSchemeChangeEvent);
        Notifications.subscribe(ConnectionHandler.DISCONNECT_EVENT, this, this::handleDisconnection);
        visualTabOpened.addListener(((observable, oldValue, newValue) -> {
            if (!newValue.equals(oldValue) && newValue.equals(true)) {
                visualViewHandler.playAnimation();
            }
        }));
        server.bind(Controllers.getConnectionHandler().getAddress());
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

//    private final Service<Void> exitTask = CommandService.getNoArgs("exit", this::handleActionResult);

    private final Service<Void> exitTask = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    Controllers.getThreadHandler().stop();
                    return null;
                }
            };
        }
    };

    public void delete() {
        deleteTask.restart();
    }

    private final Service<Void> deleteTask = CommandService.getSimpleArgs("remove_by_id", this::deleteEvent, () ->
            selectedItemProperty.getValue().getId().toString());


    private void deleteEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
    }

    public void clear() {
        clearTask.restart();
    }

    private final Service<Void> clearTask = CommandService.getNoArgs("clear", this::handleActionResult);

    public void add() {
        Optional<Map<String, String>> mapOptional = addDialog.getDialog().showAndWait();
        if (mapOptional.isEmpty()) return;
        dialogAnswer = mapOptional.get();
        addTask.restart();
    }

    private final Service<Void> addTask = CommandService.getComplexArgs("add", this::addEvent, this::getDialogAnswer);

    private void addEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
    }


    public void edit() {
        editDialog.setValues(selectedItemProperty.getValue().toModel());
        Optional<Map<String, String>> mapOptional = editDialog.getDialog().showAndWait();
        if (mapOptional.isEmpty()) return;
        dialogAnswer = mapOptional.get();
        editTask.restart();
    }

    private final Service<Void> editTask = CommandService.getBothArgs("update", this::editEvent, () ->
            selectedItemProperty.getValue().getId().toString(), this::getDialogAnswer);

    private void editEvent(ActionResult actionResult) {
        handleActionResult(actionResult);
    }

    public void filter() {
        Optional<Map<String, String>> mapOptional = filterDialog.getDialog().showAndWait();
        if (mapOptional.isEmpty()) return;
        dialogAnswer = mapOptional.get();
        filterTask.restart();
    }

    private final Service<Void> filterTask = new Service<>() {
        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() {
                    observableCollection.setFilter(dialogAnswer);
                    return null;
                }
            };
        }
    };

    private void accountChangeEvent(String event) {
        account.setValue(CurrentAccount.getAccount().getName());
    }

    private void collectibleSchemeChangeEvent(String event) {
        observableCollection = new ObservableCollection<>(Controllers.getCollectionClassesHandler().getCurrentCollectionHandler());
        visualViewHandler = new VisualViewHandler<>(observableCollection);
        TableViewHandler<? extends MainCollectible<?>> tableViewHandler = new TableViewHandler<>(observableCollection);
        visuals = visualViewHandler.getImages();
        tableView.setValue(tableViewHandler.getTableView());
        addDialog = new EditorDialog(getScheme(), new ButtonType("addButton", ButtonBar.ButtonData.APPLY));
        editDialog = new EditorDialog(getScheme(), new ButtonType("editButton", ButtonBar.ButtonData.APPLY));
        filterDialog = new FilterDialog(getScheme(), new ButtonType("filterButton", ButtonBar.ButtonData.APPLY));
    }

    @Override
    public BooleanBinding isTaskRunning() {
        return Stream.of(deleteTask, exitTask, disconnectTask, clearTask, addTask, editTask, filterTask).
                map(Service::runningProperty).map(property -> property.isEqualTo(TRUE)).reduce(Bindings::or).get();
    }

    public BooleanBinding isItemSelected() {
        BooleanBinding tableBind = tableTabOpened.isEqualTo(TRUE).and(tableView.getValue().getSelectionModel().selectedItemProperty().isNotNull()).and(
                Bindings.select(tableView.getValue().getSelectionModel().selectedItemProperty(), "owner").isEqualTo(account));
        BooleanBinding visualBind = visualTabOpened.isEqualTo(TRUE).and(visualViewHandler.getSelected().isNotNull()).and(
                Bindings.select(visualViewHandler.getSelected(), "owner").isEqualTo(account));
        return Bindings.or(tableBind, visualBind);
    }

    public CollectibleScheme getScheme() {
        return Controllers.getCollectionClassesHandler().getCurrentCollectionHandler().getCollectibleScheme();
    }
}
