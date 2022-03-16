package commands;

/**
 * A class that represents result of performing command's action
 */
public class ActionResult {
    private final boolean success;
    private final String message;

    public ActionResult(boolean result, String message) {
        this.success = result;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
