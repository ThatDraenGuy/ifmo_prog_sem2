package collection;


import collection.classes.*;

import java.util.PriorityQueue;


/**
 * A class that manages a collection. Has methods for all interactions with it and also handles collection's construction/deconstruction.
 */
public class DragonCollectionHandler extends AbstractCollectionHandler<Dragon> {
    private final CollectionBuilder builder;

    public DragonCollectionHandler(StorageHandler<MainCollectible<Dragon>> storageHandler) {
        super(storageHandler, new PriorityQueue<>(), new DragonFactory(), Dragon.class);
        this.builder = new CollectionBuilder();
    }


    @Override
    public Dragon handleRawCollectible(RawCollectible<Dragon> rawCollectible) {
        return factory.getObject(rawCollectible);
    }

    @Override
    public Dragon handleRawCollectible(RawCollectible<Dragon> rawCollectible, long id) {
        return factory.getObject(rawCollectible, id);
    }

}
