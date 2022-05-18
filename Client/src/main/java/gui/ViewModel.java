package gui;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;

public interface ViewModel {
    StringProperty getMessage();

    StringProperty getErrorMessage();

    BooleanBinding taskRunningProperty();
}
