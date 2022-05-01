package collection.classes;

import collection.meta.CollectibleModel;
import exceptions.IncorrectCollectibleTypeException;

import java.time.ZonedDateTime;

public interface MainCollectibleFactory<T extends MainCollectible<T>> extends CollectibleFactory<T> {

    T getObject(CollectibleModel collectibleModel, long id) throws IncorrectCollectibleTypeException;

    T getObject(CollectibleModel collectibleModel) throws IncorrectCollectibleTypeException;
}
