package collection;

import collection.classes.MainCollectible;
import utility.QueueWithID;

public interface CollectionSetupper<T extends MainCollectible<T>> {
    ClientCollectionHandler<T> setup();

    ClientCollectionHandler<T> setup(QueueWithID<? extends MainCollectible<?>> collection);
}
