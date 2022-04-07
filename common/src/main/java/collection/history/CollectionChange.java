package collection.history;

import collection.classes.MainCollectible;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;

public class CollectionChange<T extends MainCollectible<T>> implements Serializable {
    @Getter
    private final long newCollectionId;
    @Getter
    private final Collection<T> addedElements;
    @Getter
    private final Collection<T> removedElements;
    @Getter
    private final Class<T> elementClass;

    public CollectionChange(Collection<T> addedElements, Collection<T> removedElements, long newCollectionId, Class<T> elementClass) {
        this.newCollectionId = newCollectionId;
        this.addedElements = addedElements;
        this.removedElements = removedElements;
        this.elementClass = elementClass;
    }

    @Override
    public String toString() {
        return "newId: " + newCollectionId + ", added: " + addedElements + ", removed: " + removedElements;
    }
}
