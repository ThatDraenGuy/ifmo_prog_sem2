package commands;

import lombok.Getter;

import java.io.Serializable;

/**
 * A class that represents result of performing command's action
 */
public class ActionResult implements Serializable {
    @Getter
    private final boolean success;
    @Getter
    private final String message;

    public ActionResult(boolean result, String message) {
        this.success = result;
        this.message = message;
    }
}
