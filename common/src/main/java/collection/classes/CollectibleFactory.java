package collection.classes;

import collection.meta.CollectibleModel;
import exceptions.IncorrectCollectibleTypeException;

public interface CollectibleFactory<T extends Collectible> {
    T getObject(CollectibleModel collectibleModel) throws IncorrectCollectibleTypeException;

}
