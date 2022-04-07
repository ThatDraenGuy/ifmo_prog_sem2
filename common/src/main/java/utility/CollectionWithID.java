package utility;

import java.util.Collection;

public interface CollectionWithID<T> extends Collection<T> {
    long getId();

    void setId(long id);

    void incrementId();
}
