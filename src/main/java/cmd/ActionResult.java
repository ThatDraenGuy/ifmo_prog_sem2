package cmd;

public class ActionResult {
    private boolean success;
    private String message;
    public ActionResult(boolean result, String message) {
        this.success=result;
        this.message=message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
