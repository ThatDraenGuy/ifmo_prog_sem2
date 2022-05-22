package exceptions;

public class ElementIdException extends Exception {
    public ElementIdException(String id) {
        super(id);
    }
}
