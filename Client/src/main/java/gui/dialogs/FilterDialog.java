package gui.dialogs;

import collection.meta.CollectibleScheme;
import collection.meta.FieldData;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import locales.I18N;

import java.util.HashMap;
import java.util.Map;

public class FilterDialog extends EditorDialog {
    protected Map<String, CheckBox> checkBoxes;

    public FilterDialog(CollectibleScheme scheme, ButtonType buttonType) {
        super(scheme, buttonType);
    }

    @Override
    protected void handleData(GridPane grid, String fieldName, FieldData data) {
        Label label = new Label();
        CheckBox checkBox = new CheckBox();
        label.textProperty().bind(I18N.getCollectibleBinding(fieldName, ":"));
        if (data.isCollectible()) {
            addRow(grid, label);
            handleCollectible(grid, data.getCollectibleScheme());
            return;
        }
        Control choice = generateChoice(data, data.getType(), fieldName);
        label.disableProperty().bind(checkBox.selectedProperty().not());
        choice.disableProperty().bind(checkBox.selectedProperty().not());
        addRow(grid, checkBox, label, choice);
        if (checkBoxes == null) checkBoxes = new HashMap<>();
        checkBoxes.put(fieldName, checkBox);
    }

    @Override
    protected void setResult(Dialog<Map<String, String>> dialog, ButtonType buttonType) {
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.equals(buttonType)) {
                Map<String, String> map = new HashMap<>();
                for (String key : input.keySet()) {
                    if (checkBoxes.containsKey(key) && checkBoxes.get(key).isSelected()) {
                        Object value = input.get(key).getValue();
                        if (value == null) map.put(key, "");
                        else map.put(key, value.toString());
                    }
                }
                return map;
            }
            return null;
        });
    }

    @Override
    protected BooleanBinding createValidatedBinding() {
        return Bindings.createBooleanBinding(() -> true);
    }
}
