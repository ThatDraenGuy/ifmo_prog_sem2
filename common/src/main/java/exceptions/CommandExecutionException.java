package exceptions;

import cmd.ActionResult;

public class CommandExecutionException extends Exception {
    public CommandExecutionException(ActionResult result) {
        super(result.getMessage());
    }
}
