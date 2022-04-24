package collection.storage;

import collection.classes.MainCollectible;
import collection.meta.CollectibleModel;
import collection.meta.CollectibleScheme;
import collection.meta.InputtedValue;
import exceptions.StorageException;
import utility.CollectionWithID;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public interface StorageHandler {
//    <T extends MainCollectible<T>> void save(CollectionWithID<T> collection, Class<T> target) throws IOException;

    CollectionWithID<CollectibleModel> load(CollectibleScheme collectibleScheme) throws StorageException;

    long insert(CollectibleModel collectibleModel) throws StorageException;

    void removeById(long id) throws StorageException;

    long getCollectionId();

}
