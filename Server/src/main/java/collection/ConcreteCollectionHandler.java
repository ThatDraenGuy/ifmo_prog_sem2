package collection;

import collection.classes.*;
import exceptions.ElementIdException;
import exceptions.InvalidCollectionException;

import java.io.IOException;
import java.util.*;

public class ConcreteCollectionHandler<T extends MainCollectible<T>> implements CollectionHandler<T> {
    final private Class<T> targetClass;
    private final Queue<T> collection;
    final private StorageHandler storageHandler;
    private final MainCollectibleFactory<T> factory;
    private final CollectionBuilder<T> collectionBuilder;

    public ConcreteCollectionHandler(StorageHandler storageHandler, Queue<T> collection, MainCollectibleFactory<T> factory, CollectionBuilder<T> collectionBuilder, Class<T> targetClass) {
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
            System.out.println("Loaded collection is invalid, initializing an empty collection...(" + e.getMessage() + ")");
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
            if (!collection.removeIf(collectible -> collectible.getId().equals(id))) {
                throw new ElementIdException(strId);
            }
        } catch (NumberFormatException e) {
            throw new ElementIdException(strId);
        }

    }

    public void removeLower(RawCollectible<T> rawObject) {
        collection.removeIf(collectible -> collectible.compareTo(rawObject) < 0);
    }

    public long countByColor(String arg) throws IllegalArgumentException {
        Color color = Color.valueOf(arg);
        return collection.stream().filter(x -> x.getColor().equals(color)).count();
    }

    public String filterByType(String arg) throws IllegalArgumentException {
        DragonType type = DragonType.valueOf(arg);
        StringBuilder str = new StringBuilder();
        collection.stream().filter(x -> x.getType() != null && x.getType().equals(type)).forEach(x -> str.append(x).append("\n"));
        return str.toString();
    }

    public String filterGreaterThanAge(String arg) {
        long age = Long.parseLong(arg);
        StringBuilder str = new StringBuilder();
        collection.stream().filter(x -> x.getAge() != null && x.getAge() > age).forEach(x -> str.append(x).append("\n"));
        return str.toString();
    }

    public Long checkIds(Collection<T> collection) throws InvalidCollectionException {
        Set<Long> ids = new HashSet<>();
        long highestId = 0L;
        for (MainCollectible<?> collectible : collection) {
            long id = collectible.getId();
            boolean res = ids.add(id);
            if (!res) throw new InvalidCollectionException();
            if (id > highestId) highestId = id;
        }
        return highestId;
    }

    public String info() {
        return "This collection's type is a " + collection.getClass().getName() + ", it contains " + collection.size() + " elements.";
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        collection.forEach(x -> str.append(x).append("\n"));
        return str.toString();
    }

    private T handleRawCollectible(RawCollectible<T> rawCollectible) {
        return factory.getObject(rawCollectible);
    }

    private T handleRawCollectible(RawCollectible<T> rawCollectible, long id) {
        return factory.getObject(rawCollectible, id);
    }

}
