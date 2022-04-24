package collection;

import collection.classes.*;
import collection.meta.CollectibleScheme;
import exceptions.ElementIdException;
import exceptions.InvalidCollectionException;
import lombok.Getter;
import utility.QueueWithID;

import java.util.*;

public class CollectionHandler<T extends MainCollectible<T>> {
    protected final Class<T> targetClass;
    @Getter
    protected final CollectibleScheme collectibleScheme;
    protected QueueWithID<T> collection;

    public CollectionHandler(QueueWithID<T> collection, Class<T> targetClass) {
        this.targetClass = targetClass;
        this.collection = collection;
        this.collectibleScheme = new CollectibleScheme(targetClass);
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
        str.append("Collection id: ").append(collection.getId()).append("\n");
        collection.forEach(x -> str.append(x).append("\n"));
        return str.toString();
    }

    public long getId() {
        return collection.getId();
    }
}
