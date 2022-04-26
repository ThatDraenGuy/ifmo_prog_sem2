package collection;

import collection.classes.MainCollectible;
import collection.meta.CollectibleModel;
import exceptions.ElementIdException;
import exceptions.IncorrectCollectibleTypeException;
import exceptions.StorageException;

public class CollectionBridge<T extends MainCollectible<T>> {
    private final ServerCollectionHandler<T> collectionHandler;

    public CollectionBridge(ServerCollectionHandler<T> collectionHandler) {
        this.collectionHandler = collectionHandler;
    }

    public void add(CollectibleModel collectibleModel) throws IncorrectCollectibleTypeException, StorageException {
        check(collectibleModel);
        collectionHandler.add(collectibleModel);
    }

    public void update(String arg, String owner, CollectibleModel collectibleModel) throws IncorrectCollectibleTypeException, ElementIdException, StorageException {
        check(collectibleModel);
        collectionHandler.update(arg, owner, collectibleModel);
    }

    public void removeLower(CollectibleModel collectibleModel, String owner) throws IncorrectCollectibleTypeException, StorageException {
        check(collectibleModel);
        collectionHandler.removeLower(collectibleModel, owner);
    }

    private void check(CollectibleModel collectibleModel) {

    }

//    private RawCollectible<T> cast(RawCollectible<?> rawCollectible) throws IncorrectCollectibleTypeException {
//        try {
//            @SuppressWarnings({"unchecked"})
//            RawCollectible<T> castedCollectible = (RawCollectible<T>) rawCollectible;
//            return castedCollectible;
//        } catch (ClassCastException e) {
//            throw new IncorrectCollectibleTypeException();
//        }
//    }
}
