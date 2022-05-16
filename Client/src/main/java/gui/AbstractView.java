package gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public abstract class AbstractView {
    protected void defaultInitialize(BooleanProperty progressVisibility, StringProperty message, ViewModel viewModel) {
        progressVisibility.bind(viewModel.isTaskRunning());
        message.bind(viewModel.getMessage());
        viewModel.getErrorMessage().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue != null && !newValue.isEmpty()) {
                        Alert alert = new Alert(
                                Alert.AlertType.ERROR, newValue
                        );
                        alert.showAndWait();
                    }
                }
        );
    }

    public abstract void initialize();
}
