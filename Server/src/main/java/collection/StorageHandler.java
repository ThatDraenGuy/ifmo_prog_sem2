package collection;

import collection.classes.MainCollectible;
import utility.CollectionWithID;

import java.io.IOException;
import java.util.Map;

public interface StorageHandler {
    <T extends MainCollectible<T>> void save(CollectionWithID<T> collection, Class<T> target) throws IOException;

    <T extends MainCollectible<T>> CollectionWithID<Map<String, Object>> load(Class<T> target);

}
