package collection;

import collection.classes.MainCollectible;
import utility.ListAndId;

public interface CollectionSetupper<T extends MainCollectible<T>> {
    ClientCollectionHandler<T> setup();

    ClientCollectionHandler<T> setup(ListAndId<? extends MainCollectible<?>> collection);
}
