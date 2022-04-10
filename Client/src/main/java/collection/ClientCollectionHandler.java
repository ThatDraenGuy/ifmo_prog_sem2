package collection;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import exceptions.CollectionVersionIsBehindException;
import utility.PriorityQueueWithID;
import utility.QueueWithID;

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
        @SuppressWarnings({"unchecked"})
        CollectionChange<T> castedCollectionChange = (CollectionChange<T>) collectionChange;
        castedCollectionChange.apply(collection);
    }

    public void setCollection(QueueWithID<? extends MainCollectible<?>> collection) {
        @SuppressWarnings({"unchecked"})
        QueueWithID<T> castedCollection = (QueueWithID<T>) collection;
        this.collection = castedCollection;
    }

    public CollectionBuilder<T> getCollectionBuilder() {
        return collectionBuilder;
    }
}
