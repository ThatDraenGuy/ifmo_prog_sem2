package collection;

import collection.classes.MainCollectible;
import collection.history.CollectionChange;
import com.sun.javafx.collections.ObservableListWrapper;
import exceptions.CollectionVersionIsBehindException;
import exceptions.IncorrectCollectibleTypeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import utility.ListAndId;

public class ClientCollectionHandler<T extends MainCollectible<?>> extends CollectionHandler<T> {
    @Getter
    private CollectionChange<T> lastChange;

    public ClientCollectionHandler(Class<T> targetClass) {
        super(targetClass);
    }

    public ClientCollectionHandler(Class<T> targetClass, ListAndId<T> collection) {
        super(targetClass);
        this.collection = collection;
    }

    public void applyChange(CollectionChange<? extends MainCollectible<?>> collectionChange) throws CollectionVersionIsBehindException, IncorrectCollectibleTypeException {
        final long newCollectionId = collectionChange.getNewCollectionId();
        if (!(newCollectionId - collection.getId().get() == 1)) {
            if (newCollectionId != collection.getId().get())
                throw new CollectionVersionIsBehindException("Local collection is behind");
            return;
        }
        CollectionChange<T> castedCollectionChange = castCollectionChange(collectionChange);
        castedCollectionChange.apply(collection);
        lastChange = castedCollectionChange;
    }

    public void setCollection(ListAndId<? extends MainCollectible<?>> collection) throws IncorrectCollectibleTypeException {
        this.collection = castCollection(collection);
    }

    private ListAndId<T> castCollection(ListAndId<? extends MainCollectible<?>> collection) throws IncorrectCollectibleTypeException {
        Class<? extends MainCollectible<?>> collectionClass = collection.getTargetClass();
        if (collectionClass.equals(targetClass)) {
            @SuppressWarnings({"unchecked"})
            ListAndId<T> castedCollection = (ListAndId<T>) collection;
            return castedCollection;
        }
        throw new IncorrectCollectibleTypeException();
    }

    private CollectionChange<T> castCollectionChange(CollectionChange<? extends MainCollectible<?>> collectionChange) throws IncorrectCollectibleTypeException {
        Class<? extends MainCollectible<?>> changeClass = collectionChange.getElementClass();
        if (changeClass.equals(targetClass)) {
            @SuppressWarnings({"unchecked"})
            CollectionChange<T> castedCollectionChange = (CollectionChange<T>) collectionChange;
            return castedCollectionChange;
        }
        throw new IncorrectCollectibleTypeException();
    }

    public ListAndId<T> getCollection() {
        return collection;
    }
}
