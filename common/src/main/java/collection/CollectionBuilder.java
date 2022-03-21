package collection;

import collection.classes.MainCollectible;
import collection.classes.RawCollectible;
import exceptions.InvalidCollectionException;
import exceptions.ValueNotValidException;

import java.util.Collection;
import java.util.Map;

public interface CollectionBuilder<T extends MainCollectible<T>> {
    void put(String target, String value) throws ValueNotValidException;

    void put(String target, Object value) throws ValueNotValidException;


    Collection<T> build(Collection<Map<String, Object>> map) throws InvalidCollectionException;

    RawCollectible<T> rawBuild();
}