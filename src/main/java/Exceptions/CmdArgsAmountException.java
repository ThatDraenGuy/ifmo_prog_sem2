package Exceptions;

public class CmdArgsAmountException extends Exception {
    public CmdArgsAmountException() {
        super ("Too many arguments for a command!");
    }
    public CmdArgsAmountException(String message) {
        super(message);
    }
}
