package exceptions;

import java.sql.SQLException;

public class StorageException extends Exception {
    public StorageException(Exception e) {
        super(e.getMessage());
    }
}
