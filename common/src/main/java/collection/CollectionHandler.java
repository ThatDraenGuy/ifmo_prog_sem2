package collection;

import collection.classes.*;
import collection.meta.CollectibleScheme;
import exceptions.InvalidCollectionException;
import lombok.Getter;
import utility.ListAndId;

import java.util.*;

public class CollectionHandler<T extends MainCollectible<?>> {
    protected final Class<T> targetClass;
    @Getter
    protected final CollectibleScheme collectibleScheme;
    protected ListAndId<T> collection;

    public CollectionHandler(Class<T> targetClass) {
        this.targetClass = targetClass;
        List<T> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        this.collection = new ListAndId<>(0, synchronizedList, targetClass);
        this.collectibleScheme = new CollectibleScheme(targetClass);
    }


    public long countByColor(String arg) throws IllegalArgumentException {
        Color color = Color.valueOf(arg);
        return collection.getList().stream().filter(x -> x.getColor().equals(color)).count();
    }

    public String filterByType(String arg) throws IllegalArgumentException {
        DragonType type = DragonType.valueOf(arg);
        StringBuilder str = new StringBuilder();
        collection.getList().stream().filter(x -> x.getType() != null && x.getType().equals(type)).forEach(x -> str.append(x).append("\n"));
        return str.toString();
    }

    public String filterGreaterThanAge(String arg) {
        long age = Long.parseLong(arg);
        StringBuilder str = new StringBuilder();
        collection.getList().stream().filter(x -> x.getAge() != null && x.getAge() > age).forEach(x -> str.append(x).append("\n"));
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
        return "This collection's type is a " + collection.getClass().getName() + ", it contains " + collection.getList().size() + " elements.";
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Collection id: ").append(collection.getId()).append("\n");
        collection.getList().forEach(x -> str.append(x).append("\n"));
        return str.toString();
    }

    public String filterByOwner(String owner) {
        StringBuilder builder = new StringBuilder();
        collection.getList().stream().filter(collectible -> collectible.getOwner().equals(owner)).forEach(x -> builder.append(x).append("\n"));
        return builder.toString();
    }

    public long getId() {
        return collection.getId();
    }
}
