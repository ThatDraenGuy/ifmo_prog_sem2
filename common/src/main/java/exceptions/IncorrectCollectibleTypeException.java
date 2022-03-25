package exceptions;

public class IncorrectCollectibleTypeException extends Exception {
    public IncorrectCollectibleTypeException() {
        super("Collection doesn't support this type of object");
    }
}
