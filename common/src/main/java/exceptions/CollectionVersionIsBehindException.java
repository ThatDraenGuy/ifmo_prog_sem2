package exceptions;

public class CollectionVersionIsBehindException extends Exception {
    public CollectionVersionIsBehindException(String message) {
        super(message);
    }
}
