package collection;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import exceptions.CollectionVersionIsBehindException;
import utility.ListAndId;

public class ClientCollectionHandler<T extends MainCollectible<T>> extends CollectionHandler<T> {
    public ClientCollectionHandler(Class<T> targetClass) {
        super(targetClass);
    }

    public ClientCollectionHandler(Class<T> targetClass, ListAndId<T> collection) {
        this(targetClass);
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

    public void setCollection(ListAndId<? extends MainCollectible<?>> collection) {
        @SuppressWarnings({"unchecked"})
        ListAndId<T> castedCollection = (ListAndId<T>) collection;
        this.collection = castedCollection;
    }
}
