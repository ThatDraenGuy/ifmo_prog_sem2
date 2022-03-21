package collection;

import collection.classes.MainCollectible;
import exceptions.InvalidCollectionException;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface StorageHandler {
    <T extends MainCollectible<T>> void save(Collection<T> collection, Class<T> target) throws IOException;

    <T extends MainCollectible<T>> Collection<Map<String, Object>> load(Class<T> target);

}
