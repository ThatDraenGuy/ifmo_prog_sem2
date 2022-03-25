package collection;

import collection.classes.MainCollectible;
import collection.classes.RawCollectible;
import exceptions.InvalidCollectionException;
import exceptions.ValueNotValidException;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CollectionBuilder<T extends MainCollectible<T>> {
    //    void put(String target, String value) throws ValueNotValidException;
//
//    void put(String target, Object value) throws ValueNotValidException;
    List<Field> getClassFields(Class<?> targetClass);

    Collection<T> build(Collection<Map<String, Object>> map) throws InvalidCollectionException;

    RawCollectible<T> rawBuild(Map<String, Object> map) throws ValueNotValidException;
}