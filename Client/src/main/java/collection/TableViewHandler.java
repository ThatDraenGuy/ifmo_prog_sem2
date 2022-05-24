package collection;

import collection.classes.*;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
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

import java.lang.reflect.Field;
import java.util.*;

public class TableViewHandler<T extends MainCollectible<?>> {
    @Getter
    private final TableView<T> tableView;
    private final ObservableList<T> items = FXCollections.observableArrayList();
    private final CollectibleScheme scheme;

    public TableViewHandler(ObservableCollection<T> observableCollection) {
        this.scheme = observableCollection.getCollectibleScheme();
        Bindings.bindContent(items, observableCollection.getItems());
        tableView = generate(scheme);
        tableView.setItems(items);
    }


    public TableView<T> generate(CollectibleScheme scheme) {
        TableView<T> tableView = new TableView<>();
        tableView.getColumns().addAll(create(scheme, ""));
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
        column.setSortable(true);
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
}
