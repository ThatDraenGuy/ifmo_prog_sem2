package gui.dialogs;

import collection.Validator;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import collection.meta.FieldModel;
import exceptions.ValueNotValidException;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.FormatStringConverter;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.LocalDateTimePicker;
import locales.CollectibleFormatter;
import locales.I18N;
import lombok.Getter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.Callable;


public class EditorDialog {
    private int counter = 0;
    @Getter
    private final Dialog<Map<String, String>> dialog;
    private final List<BooleanProperty> validatedProperties = new ArrayList<>();
    private final Map<String, TextField> textFields = new HashMap<>();
    private final Map<String, ChoiceBox<?>> choiceBoxes = new HashMap<>();
    protected final Map<String, ObservableValue<?>> input = new HashMap<>();
    private final ButtonType cancelButton = new ButtonType("cancelButton", ButtonBar.ButtonData.CANCEL_CLOSE);
    private final Border invalidBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN));
    private final Border validBorder = new Border(new BorderStroke(Color.LIME, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN));
    private final Border defaultBorder = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderStroke.THIN));

    public EditorDialog(CollectibleScheme scheme, ButtonType buttonType) {
        this.dialog = create(scheme, buttonType);
    }

    protected Dialog<Map<String, String>> create(CollectibleScheme scheme, ButtonType buttonType) {
        Dialog<Map<String, String>> dialog = new Dialog<>();
        //Buttons
        dialog.getDialogPane().getButtonTypes().addAll(buttonType, cancelButton);
        //Grid for fields
        dialog.getDialogPane().setContent(createGrid(scheme));
        //ValidatedBinding
        dialog.getDialogPane().lookupButton(buttonType).disableProperty().bind(createValidatedBinding().not());
        setResult(dialog, buttonType);
        for (ButtonType type : dialog.getDialogPane().getButtonTypes()) {
            Button button = (Button) dialog.getDialogPane().lookupButton(type);
            button.textProperty().bind(I18N.getGuiLabelBinding(button.getText()));
        }
        return dialog;
    }

    protected void setResult(Dialog<Map<String, String>> dialog, ButtonType buttonType) {
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.equals(buttonType)) {
                Map<String, String> map = new HashMap<>();
                for (String key : input.keySet()) {
                    Object value = input.get(key).getValue();
                    if (value == null) map.put(key, "");
                    else map.put(key, value.toString());
                }
                return map;
            }
            return null;
        });
    }

    protected BooleanBinding createValidatedBinding() {
        Optional<BooleanBinding> bindingOptional = validatedProperties.stream().map(booleanProperty -> booleanProperty.isEqualTo(new SimpleBooleanProperty(true))).reduce(Bindings::and);
        return bindingOptional.orElseGet(() -> new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return true;
            }
        });
    }

    protected GridPane createGrid(CollectibleScheme scheme) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        handleCollectible(grid, scheme);
        return grid;
    }

    protected void handleCollectible(GridPane grid, CollectibleScheme scheme) {
        for (String fieldName : scheme.getFieldsData().keySet()) {
            FieldData data = scheme.getFieldsData().get(fieldName);
            if (data.isUserWritable()) {
                handleData(grid, fieldName, data);
            }
        }
    }

    protected void handleData(GridPane grid, String fieldName, FieldData data) {
        Label label = new Label();
        label.textProperty().bind(I18N.getCollectibleBinding(fieldName, ":"));
        if (data.isCollectible()) {
            addRow(grid, label);
            handleCollectible(grid, data.getCollectibleScheme());
            return;
        }
        Control choice = generateChoice(data, data.getType(), fieldName);
        addRow(grid, label, choice);
    }

    protected void addRow(GridPane grid, Node... row) {
        grid.addRow(counter, row);
        counter++;
    }

    protected <T> Control generateChoice(FieldData fieldData, Class<T> fieldType, String fieldName) {
        BooleanProperty booleanProperty = new SimpleBooleanProperty();
        Control control;
        if (fieldType.isEnum()) {
            ChoiceBox<T> box = new ChoiceBox<>(FXCollections.observableArrayList(fieldType.getEnumConstants()));
            choiceBoxes.put(fieldName, box);
            input.put(fieldName, box.valueProperty());
            if (!fieldData.isNotNull()) box.getItems().add(null);
            box.valueProperty().addListener(((observable, oldValue, newValue) -> {
                try {
                    Validator.validate(fieldData, newValue);
                    booleanProperty.setValue(true);
                    box.setBorder(validBorder);
                } catch (ValueNotValidException e) {
                    booleanProperty.setValue(false);
                    box.setBorder(invalidBorder);
                }
            }));
            control = box;
        } else if (Temporal.class.isAssignableFrom(fieldType)) {
            LocalDatePicker datePicker = new LocalDatePicker();
            datePicker.localeProperty().bind(I18N.getLocale());
            datePicker.setPrefWidth(datePicker.getPrefWidth() + 400);
            input.put(fieldName, datePicker.localDateProperty());
            control = datePicker;
        } else {
            TextField textField = new TextField();
            textFields.put(fieldName, textField);
            textField.promptTextProperty().bind(I18N.getCollectibleBinding(fieldName));
            input.put(fieldName, textField.textProperty());
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    Validator.convertAndValidate(fieldData, fieldType, newValue);
                    booleanProperty.setValue(true);
                    textField.setBorder(validBorder);
                } catch (ValueNotValidException e) {
                    booleanProperty.setValue(false);
                    textField.setBorder(invalidBorder);
                }
            });
            control = textField;
        }
        validatedProperties.add(booleanProperty);
        control.setBorder(defaultBorder);
        return control;
    }

    public void setValues(CollectibleModel collectibleModel) {
        for (String field : collectibleModel.getValues().keySet()) {
            FieldModel model = collectibleModel.getValues().get(field);
            if (model.getFieldData().isCollectible()) setValues(model.getCollectibleModel());
            else {
                TextField textField = textFields.get(field);
                String value = model.getValue() == null ? "" : model.getValue().toString();
                if (textField != null) textField.setText(value);
                ChoiceBox<?> choiceBox = choiceBoxes.get(field);
                if (choiceBox != null) {
                    setChoiceBox(choiceBox, model);
                }
            }
        }
    }

    protected <T> void setChoiceBox(ChoiceBox<T> box, FieldModel model) {
        T value = (T) model.getValue();
        box.setValue(value);
    }

}
