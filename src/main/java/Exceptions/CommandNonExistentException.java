package Exceptions;

public class CommandNonExistentException extends Exception {
    public CommandNonExistentException(String cmd) {
        super("Command \""+cmd+"\" does not exist!");
    }
}
