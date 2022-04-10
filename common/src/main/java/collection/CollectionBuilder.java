package collection;

import collection.classes.MainCollectible;
import collection.classes.RawCollectible;
import exceptions.InvalidCollectionException;
import exceptions.ValueNotValidException;
import utility.CollectionWithID;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface CollectionBuilder<T extends MainCollectible<T>> {

    List<Field> getClassFields(Class<?> targetClass);

    CollectionWithID<T> build(CollectionWithID<Map<String, Object>> map) throws InvalidCollectionException;

    RawCollectible<T> rawBuild(Map<String, Object> map) throws ValueNotValidException;
}