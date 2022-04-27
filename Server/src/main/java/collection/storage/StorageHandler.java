package collection.storage;

import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import exceptions.StorageException;
import utility.ListAndId;

import java.util.Collection;

public interface StorageHandler {
//    <T extends MainCollectible<T>> void save(CollectionWithID<T> collection, Class<T> target) throws IOException;

    ListAndId<CollectibleModel> load(CollectibleScheme collectibleScheme) throws StorageException;

    long insert(CollectibleModel collectibleModel) throws StorageException;

    boolean update(long id, String owner, CollectibleModel collectibleModel) throws StorageException;

    boolean removeById(long id, String owner) throws StorageException;

    boolean removeABunch(Collection<Long> ids, String owner) throws StorageException;

    void clear(String owner) throws StorageException;

    long getCollectionId();

}
