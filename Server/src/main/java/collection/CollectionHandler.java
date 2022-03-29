package collection;

import collection.classes.MainCollectible;
import collection.classes.RawCollectible;
import exceptions.ElementIdException;
import exceptions.InvalidCollectionException;

import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

public interface CollectionHandler<T extends MainCollectible<T>> {
    void add(RawCollectible<T> rawCollectible);

    void update(String arg, RawCollectible<T> rawCollectible) throws ElementIdException;

    void clear();

    void removeFirst() throws NoSuchElementException;

    void removeById(String strId) throws ElementIdException;

    void removeLower(RawCollectible<T> rawCollectible);

    long countByColor(String color) throws IllegalArgumentException;

    String filterByType(String arg) throws IllegalArgumentException;

    String filterGreaterThanAge(String age);

    String info();

    Long checkIds(Collection<T> collection) throws InvalidCollectionException;

    void load();

    void save() throws IOException;
}
