package Exceptions;

import cmd.ActionResult;
import common.Response;

public class CommandExecutionException extends Exception {
    public CommandExecutionException(ActionResult result) {
        super(result.getMessage());
    }
}
