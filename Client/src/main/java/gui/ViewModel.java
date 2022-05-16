package gui;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;

public interface ViewModel {
    StringProperty getMessage();

    StringProperty getErrorMessage();

    ReadOnlyBooleanProperty isTaskRunning();
}
