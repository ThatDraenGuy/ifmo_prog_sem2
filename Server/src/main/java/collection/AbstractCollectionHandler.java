package collection;

import collection.classes.*;
import exceptions.ElementIdException;
import exceptions.InvalidCollectionException;

import java.io.IOException;
import java.util.*;

public class AbstractCollectionHandler<T extends MainCollectible<T>> implements CollectionHandler<T> {
    final private Class<T> targetClass;
    private final Queue<T> collection;
    final private StorageHandler storageHandler;
    private final MainCollectibleFactory<T> factory;
    private final CollectionBuilder<T> collectionBuilder;

    public AbstractCollectionHandler(StorageHandler storageHandler, Queue<T> collection, MainCollectibleFactory<T> factory, CollectionBuilder<T> collectionBuilder, Class<T> targetClass) {
        this.storageHandler = storageHandler;
        this.targetClass = targetClass;
        this.collection = collection;
        this.factory = factory;
        this.collectionBuilder = collectionBuilder;
    }

    public void save() throws IOException {
        storageHandler.save(collection, targetClass);
    }

    public void load() {
        try {
            Collection<Map<String, Object>> loadedCollection = this.storageHandler.load(targetClass);
            Collection<T> collection = collectionBuilder.build(loadedCollection);
            long id = checkIds(collection);
            this.collection.addAll(collection);
            factory.setNextId(id + 1);
        } catch (InvalidCollectionException e) {
            System.out.println("Loaded collection is invalid, initializing an empty collection...");
        }
    }

    public void clear() {
        collection.clear();
    }

    public void add(RawCollectible<T> rawObject) {
        collection.add(handleRawCollectible(rawObject));
    }

    public void update(String arg, RawCollectible<T> rawCollectible) throws ElementIdException {
        long id = Long.parseLong(arg);
        T newObject = handleRawCollectible(rawCollectible, id);
        boolean result = collection.removeIf(dragon -> dragon.getId().equals(id));
        if (result) {
            collection.add(newObject);
        } else {
            throw new ElementIdException(arg);
        }
    }

    public void removeFirst() throws NoSuchElementException {
        this.collection.remove();
    }

    public void removeById(String strId) throws ElementIdException {
        try {
            long id = Long.parseLong(strId);
            if (!collection.removeIf(collectible -> collectible.getId() == id)) {
                throw new ElementIdException(strId);
            }
        } catch (NumberFormatException e) {
            throw new ElementIdException(strId);
        }

    }

    public void removeLower(RawCollectible<T> rawObject) {
        collection.removeIf(collectible -> collectible.compareTo(rawObject) < 0);
    }

    public int countByColor(String arg) throws IllegalArgumentException {
        Color target = Color.valueOf(arg);
        int res = 0;
        for (MainCollectible<?> collectible : collection) {
            if (collectible.getColor().equals(target)) {
                res++;
            }
        }
        return res;
    }

    public String filterByType(String arg) throws IllegalArgumentException {
        DragonType type = DragonType.valueOf(arg);
        StringBuilder str = new StringBuilder();
        for (MainCollectible<?> collectible : collection) {
            DragonType collectibleType = collectible.getType();
            if (collectibleType != null && collectibleType.equals(type)) {
                str.append(collectible).append("\n");
            }
        }
        return str.toString();
    }

    public String filterGreaterThanAge(String arg) {
        long age = Long.parseLong(arg);
        StringBuilder str = new StringBuilder();
        for (MainCollectible<?> collectible : collection) {
            Long collectibleAge = collectible.getAge();
            if (collectibleAge != null && collectibleAge > age) {
                str.append(collectible).append("\n");
            }
        }
        return str.toString();
    }

    public Long checkIds(Collection<T> collection) throws InvalidCollectionException {
        HashSet<Long> ids = new HashSet<>();
        for (MainCollectible<?> collectible : collection) {
            boolean res = ids.add(collectible.getId());
            if (!res) {
                throw new InvalidCollectionException();
            }
        }
        long highestId = 0L;
        for (long id : ids) {
            if (id > highestId) {
                highestId = id;
            }
        }
        return highestId;
    }

    public String info() {
        return "This collection's type is a " + collection.getClass().getName() + ", it contains " + collection.size() + " elements.";
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (MainCollectible<?> collectible : collection) {
            str.append(collectible.toString()).append("\n");
        }
        return str.toString();
    }

    public T handleRawCollectible(RawCollectible<T> rawCollectible) {
        return factory.getObject(rawCollectible);
    }

    public T handleRawCollectible(RawCollectible<T> rawCollectible, long id) {
        return factory.getObject(rawCollectible, id);
    }

}
