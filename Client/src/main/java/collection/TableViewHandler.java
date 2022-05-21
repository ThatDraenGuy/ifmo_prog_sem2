package collection;

import collection.classes.*;
import collection.history.CollectionChange;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import javafx.beans.binding.*;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import locales.I18N;
import lombok.Getter;
import utility.ListAndId;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.Callable;

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

    public void applyChange(CollectionChange<T> change) {
        Collection<T> added = change.getAddedElements();
        Collection<T> removed = change.getRemovedElements();
        tableView.getItems().addAll(added);
        tableView.getItems().removeAll(removed);
    }

    public void put(ListAndId<T> listAndId) {
        for (T element : listAndId.getList()) {
            tableView.getItems().add(element);
        }
        autoResizeColumns(tableView);
    }

    public void set(ListAndId<T> listAndId) {
        tableView.getItems().clear();
        put(listAndId);
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
            if (data.isUserReadable()) list.add(genColumn(data, data.getType(), schemeName, fieldName));
        }
        return list;
    }

    private <S> TableColumn<T, S> genColumn(FieldData data, Class<S> dataClass, String schemeName, String fieldName) {
        TableColumn<T, S> column = new TableColumn<>(fieldName);
        column.textProperty().bind(I18N.getCollectibleBinding(fieldName));
        column.textProperty().addListener(((observable, oldValue, newValue) -> autoResizeColumns(tableView)));
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
        if (Temporal.class.isAssignableFrom(dataClass)) {
            column.cellFactoryProperty().bind(Bindings.createObjectBinding(() -> new Callback<>() {
                @Override
                public TableCell<T, S> call(TableColumn<T, S> param) {
                    return new TableCell<>() {
                        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy - hh:mm (zzzz)", I18N.getLocale().get());

                        @Override
                        protected void updateItem(S item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setText(null);
                            } else {
                                this.setText(formatter.format((TemporalAccessor) item));
                            }
                        }
                    };
                }
            }, I18N.getLocale()));
        }
        return column;
    }

    public static void autoResizeColumns(TableView<?> table) {
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().forEach((column) -> {
            Text header = new Text(column.getText());
            double max = header.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                if (column.getCellData(i) != null) {
                    Text word = new Text(column.getCellData(i).toString());
                    double newWidth = word.getLayoutBounds().getWidth();
                    if (newWidth > max) {
                        max = newWidth;
                    }
                }
            }
            column.setPrefWidth(max + 10.0d);
        });
//        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
//        table.getColumns().forEach((column) -> {
//            StringProperty stringProperty = column.textProperty();
//            IntegerBinding lengthBind = stringProperty.length();
//            NumberBinding finalBinding = lengthBind;
//            for (int i = 0; i < table.getItems().size(); i++) {
//                if (column.getCellData(i) != null) {
//                    int finalI = i;
//                    DoubleBinding binding = Bindings.createDoubleBinding(() -> {
//                        Text word = new Text(column.getCellData(finalI).toString());
//                        double width = word.getLayoutBounds().getWidth();
//                        return width;
//                    }, I18N.getLocale());
//                    finalBinding = Bindings.max(finalBinding,binding);
//                }
//            }
//            column.prefWidthProperty().bind(finalBinding.add(10.0d));
//        });
    }
}
