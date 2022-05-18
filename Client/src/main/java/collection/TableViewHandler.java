package collection;

import collection.classes.*;
import collection.history.CollectionChange;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import lombok.Getter;
import utility.ListAndId;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class TableViewHandler<T extends MainCollectible<?>> {
    @Getter
    private final TableView<T> tableView;
    private final Class<T> targetClass;

    public TableViewHandler(Class<T> targetClass) {
        this.targetClass = targetClass;
        tableView = generate(targetClass);
    }

    public TableViewHandler(Class<T> targetClass, ListAndId<T> listAndId) {
        this.targetClass = targetClass;
        tableView = generate(targetClass);
        put(listAndId);
    }

    public void applyChange(CollectionChange<? extends MainCollectible<?>> collectionChange) {
        if (collectionChange.getElementClass().equals(Dragon.class)) {
            CollectionChange<T> change = (CollectionChange<T>) collectionChange;
            Collection<T> added = change.getAddedElements();
            Collection<T> removed = change.getRemovedElements();
            tableView.getItems().addAll(added);
            tableView.getItems().removeAll(removed);
        }
    }

    public void put(ListAndId<T> listAndId) {
        for (T element : listAndId.getList()) {
            tableView.getItems().add(element);
        }
        autoResizeColumns(tableView);
    }

    public void set(ListAndId<? extends MainCollectible<?>> listAndId) {
        tableView.getItems().clear();
        put((ListAndId<T>) listAndId);
    }


    public TableView<T> generate(Class<T> targetClass) {
        TableView<T> tableView = new TableView<>();
        tableView.getColumns().addAll(create(new CollectibleScheme(targetClass), ""));
        autoResizeColumns(tableView);
        return tableView;
    }

    private List<TableColumn<T, ?>> create(CollectibleScheme scheme, String schemeName) {
        List<TableColumn<T, ?>> list = new ArrayList<>();
        for (String fieldName : scheme.getFieldsData().keySet()) {
            FieldData data = scheme.getFieldsData().get(fieldName);
            if (data.isUserReadable()) list.add(genColumn(data, data.getClass(), schemeName, fieldName));
        }
        return list;
    }

    private <S> TableColumn<T, S> genColumn(FieldData data, Class<S> dataClass, String schemeName, String fieldName) {
        TableColumn<T, S> column = new TableColumn<>(fieldName);
        if (data.isCollectible()) {
            column.getColumns().addAll(create(data.getCollectibleScheme(), fieldName));
            return column;
        }
        if (!schemeName.equals("")) {
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<T, S>, ObservableValue<S>>() {
                @Override
                public ObservableValue<S> call(TableColumn.CellDataFeatures<T, S> param) {
                    T object = param.getValue();
                    try {
                        Field field = object.getClass().getDeclaredField(schemeName);
                        field.setAccessible(true);
                        Object collectible = field.get(object);
                        Field finalField = collectible.getClass().getDeclaredField(fieldName);
                        finalField.setAccessible(true);
                        Object res = finalField.get(collectible);
                        return new ObservableValueBase<S>() {
                            @Override
                            public S getValue() {
                                return (S) res;
                            }
                        };
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        return null;
                    }
                }
            });
        } else column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        return column;
    }

    public static void autoResizeColumns(TableView<?> table) {
        //Set the right policy
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().forEach((column) ->
        {
            //Minimal width = columnHeader
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                //cell must not be empty
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-width with some extra space
            column.setPrefWidth(max + 10.0d);
        });
    }
}
