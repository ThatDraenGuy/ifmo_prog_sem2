package collection;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import exceptions.CollectionVersionIsBehindException;
import utility.PriorityQueueWithID;
import utility.QueueWithID;

import java.util.Collection;

public class ClientCollectionHandler<T extends MainCollectible<T>> extends CollectionHandler<T> {
    public ClientCollectionHandler(CollectionBuilder<T> collectionBuilder, Class<T> targetClass) {
        super(new PriorityQueueWithID<>(), collectionBuilder, targetClass);
    }

    public ClientCollectionHandler(CollectionBuilder<T> collectionBuilder, Class<T> targetClass, QueueWithID<T> collection) {
        this(collectionBuilder, targetClass);
        this.collection = collection;
    }

    public void applyChange(CollectionChange<? extends MainCollectible<?>> collectionChange) throws CollectionVersionIsBehindException {
        final long newCollectionId = collectionChange.getNewCollectionId();
        if (!(newCollectionId - collection.getId() == 1)) {
            if (newCollectionId != collection.getId())
                throw new CollectionVersionIsBehindException("Local collection is behind");
            return;
        }
        doChange((CollectionChange<T>) collectionChange);
    }

    private void doChange(CollectionChange<T> collectionChange) {
        Collection<?> addedElements = collectionChange.getAddedElements();
        Collection<?> removedElements = collectionChange.getRemovedElements();
        collection.addAll(castCollection(addedElements));
        collection.removeAll(castCollection(removedElements));
        collection.setId(collectionChange.getNewCollectionId());
    }

    public void setCollection(QueueWithID<? extends MainCollectible<?>> collection) {
        this.collection = (QueueWithID<T>) collection;
    }

    public CollectionBuilder<T> getCollectionBuilder() {
        return collectionBuilder;
    }
}
