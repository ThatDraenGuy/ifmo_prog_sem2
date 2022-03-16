package exceptions;

import commands.ActionResult;

public class CommandExecutionException extends Exception {
    public CommandExecutionException(ActionResult result) {
        super(result.getMessage());
    }
}
