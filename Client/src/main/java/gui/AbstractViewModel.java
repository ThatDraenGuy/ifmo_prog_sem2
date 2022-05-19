package gui;

import commands.ActionResult;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public abstract class AbstractViewModel implements ViewModel {
    @Getter
    protected final BooleanProperty success = new SimpleBooleanProperty();
    @Getter
    protected final StringProperty message = new SimpleStringProperty("");
    @Getter
    protected final StringProperty errorMessage = new SimpleStringProperty("");
    protected final BooleanProperty TRUE = new SimpleBooleanProperty(true);

    public AbstractViewModel() {
        Notifications.subscribe(Notifications.SCENE_CHANGE_EVENT, this, this::handleSceneChange);
    }

    protected void handleSceneChange(String event) {
        message.setValue("");
        errorMessage.setValue("");
    }

    protected void handleActionResult(ActionResult actionResult) {
        success.setValue(actionResult.isSuccess());
        if (!success.get()) {
            errorMessage.setValue(actionResult.getMessage());
            message.setValue("");
            errorMessage.setValue("");
        } else {
            message.setValue(actionResult.getMessage());
        }
    }
}
