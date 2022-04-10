package collection;

import collection.classes.MainCollectible;
import collection.classes.RawCollectible;
import exceptions.ElementIdException;
import exceptions.IncorrectCollectibleTypeException;

public class CollectionBridge<T extends MainCollectible<T>> {
    private final ServerCollectionHandler<T> collectionHandler;

    public CollectionBridge(ServerCollectionHandler<T> collectionHandler) {
        this.collectionHandler = collectionHandler;
    }

    public void add(RawCollectible<?> rawCollectible) throws IncorrectCollectibleTypeException {
        collectionHandler.add(cast(rawCollectible));
    }

    public void update(String arg, RawCollectible<?> rawCollectible) throws IncorrectCollectibleTypeException, ElementIdException {
        collectionHandler.update(arg, cast(rawCollectible));
    }

    public void removeLower(RawCollectible<?> rawCollectible) throws IncorrectCollectibleTypeException {
        collectionHandler.removeLower(cast(rawCollectible));
    }

    private RawCollectible<T> cast(RawCollectible<?> rawCollectible) throws IncorrectCollectibleTypeException {
        try {
            @SuppressWarnings({"unchecked"})
            RawCollectible<T> castedCollectible = (RawCollectible<T>) rawCollectible;
            return castedCollectible;
        } catch (ClassCastException e) {
            throw new IncorrectCollectibleTypeException();
        }
    }
}
