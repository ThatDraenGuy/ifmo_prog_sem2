package exceptions;

public class ElementIdException extends Exception {
    public ElementIdException(String id) {
        super("There is no element with id \"" + id + "\" in collection!");
    }
}
