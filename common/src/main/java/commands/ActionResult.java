package commands;

import java.io.Serializable;

/**
 * A class that represents result of performing command's action
 */
public class ActionResult implements Serializable {
    private final boolean success;
    private final boolean consoleExitQueried;
    private final String message;

    public ActionResult(boolean result, String message) {
        this.success = result;
        this.message = message;
        this.consoleExitQueried = false;
    }

    public ActionResult(boolean result, String message, boolean consoleExitQueried) {
        this.success = result;
        this.message = message;
        this.consoleExitQueried = consoleExitQueried;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isConsoleExitQueried() {
        return consoleExitQueried;
    }

    public String getMessage() {
        return message;
    }
}
