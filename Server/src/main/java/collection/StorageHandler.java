package collection;

import collection.classes.MainCollectible;

import java.io.IOException;
import java.util.Collection;
import java.util.PriorityQueue;

public interface StorageHandler<T extends MainCollectible<?>> {
    void save(Collection<T> collection, Class<? extends T> target) throws IOException;

    PriorityQueue<T> load(Class<? extends T> target);

}
