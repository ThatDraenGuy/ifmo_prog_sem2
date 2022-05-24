package collection;

import collection.classes.*;
import collection.history.CollectionChange;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import collection.meta.FieldModel;
import gui.Notifications;
import javafx.beans.binding.*;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import locales.CollectibleFormatter;
import locales.I18N;
import lombok.Getter;
import utility.ListAndId;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;

public class TableViewHandler<T extends MainCollectible<?>> {
    private final ClientCollectionHandler<T> collectionHandler;
    @Getter
    private final TableView<T> tableView;
    private final ObservableList<T> items = FXCollections.observableArrayList();
    private final FilteredList<T> filteredItems = new FilteredList<>(items);
    private final Class<T> targetClass;

    public TableViewHandler(ClientCollectionHandler<T> collectionHandler) {
        this.collectionHandler = collectionHandler;
        this.targetClass = collectionHandler.getTargetClass();
        tableView = generate(targetClass);
        filteredItems.setPredicate(x -> true);
        tableView.setItems(filteredItems);
        Notifications.subscribe(CollectionClassesHandler.COLLECTION_SET_EVENT, this, this::set);
        Notifications.subscribe(CollectionClassesHandler.COLLECTION_CHANGE_EVENT, this, this::applyChange);
        put(collectionHandler.getCollection());
    }

    public void applyChange(String event) {
        CollectionChange<T> change = collectionHandler.getLastChange();
        Collection<T> added = change.getAddedElements();
        Collection<T> removed = change.getRemovedElements();
        items.addAll(added);
        items.removeAll(removed);
//        tableView.getItems().addAll(added);
//        tableView.getItems().removeAll(removed);
    }

    public void put(ListAndId<T> listAndId) {
        items.addAll(listAndId.getList());
//        for (T element : listAndId.getList()) {
//            tableView.getItems().add(element);
//        }
    }

    public void set(String event) {
        ListAndId<T> listAndId = collectionHandler.getCollection();
        items.clear();
//        tableView.getItems().clear();
        put(listAndId);
    }


    public TableView<T> generate(Class<T> targetClass) {
        TableView<T> tableView = new TableView<>();
        tableView.getColumns().addAll(create(new CollectibleScheme(targetClass), ""));
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        Label placeholder = new Label();
        placeholder.textProperty().bind(I18N.getGuiLabelBinding("emptyTable"));
        tableView.setPlaceholder(placeholder);
        return tableView;
    }

    private List<TableColumn<T, ?>> create(CollectibleScheme scheme, String schemeName) {
        List<TableColumn<T, ?>> list = new ArrayList<>();
        for (String fieldName : scheme.getFieldsData().keySet()) {
            FieldData data = scheme.getFieldsData().get(fieldName);
            if (data.isUserReadable()) list.add(genColumn(data, data.getType(), schemeName, fieldName));
        }
        return list;
    }

    private <S> TableColumn<T, S> genColumn(FieldData data, Class<S> dataClass, String schemeName, String fieldName) {
        TableColumn<T, S> column = new TableColumn<>(fieldName);
        column.textProperty().bind(I18N.getCollectibleBinding(fieldName));
        if (data.isCollectible()) {
            column.getColumns().addAll(create(data.getCollectibleScheme(), fieldName));
            return column;
        }
        if (!schemeName.equals("")) {
            column.setCellValueFactory(param -> {
                T object = param.getValue();
                try {
                    Field field = object.getClass().getDeclaredField(schemeName);
                    field.setAccessible(true);
                    Object collectible = field.get(object);
                    Field finalField = collectible.getClass().getDeclaredField(fieldName);
                    finalField.setAccessible(true);
                    Object res = finalField.get(collectible);
                    return new ObservableValueBase<>() {
                        @Override
                        public S getValue() {
                            return (S) res;
                        }
                    };
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    return null;
                }
            });
        } else column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        column.cellFactoryProperty().bind(Bindings.createObjectBinding(() -> new Callback<>() {
            @Override
            public TableCell<T, S> call(TableColumn<T, S> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(S item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            this.setText(CollectibleFormatter.formatData(data, dataClass, item));
                        }
                    }
                };
            }
        }, I18N.getLocale()));
        return column;
    }

    public void setFilter(Map<String, String> filter) {
        filteredItems.setPredicate(t -> {
            CollectibleModel model = t.toModel();
            Map<String, FieldModel> values = model.getValues();
            for (String key : filter.keySet()) {
                if (!values.containsKey(key)) return false;
                FieldModel data = values.get(key);
                Object value = data.getValue();
                String str = value == null ? "" : value.toString();
                if (!str.equals(filter.get(key))) return false;
            }
            return true;
        });
    }
}
