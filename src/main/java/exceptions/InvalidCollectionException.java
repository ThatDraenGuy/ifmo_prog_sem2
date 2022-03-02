package exceptions;

public class InvalidCollectionException extends Exception {
    public InvalidCollectionException() {
        super("Collection is invalid!");
    }
}
