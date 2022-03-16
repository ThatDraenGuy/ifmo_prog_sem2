package exceptions;

public class CommandArgsAmountException extends Exception {
    public CommandArgsAmountException() {
        super("Too many arguments for a command!");
    }

    public CommandArgsAmountException(String message) {
        super(message);
    }
}
