package collection;

import collection.classes.Dragon;
import collection.classes.DragonFactory;
import collection.classes.MainCollectible;
import utility.QueueWithID;

public class DragonCollectionSetupper implements CollectionSetupper<Dragon> {
    private final DragonCollectionBuilder collectionBuilder;

    public DragonCollectionSetupper() {
        this.collectionBuilder = new DragonCollectionBuilder(new DragonFactory());
    }

    @Override
    public ClientCollectionHandler<Dragon> setup() {
        return new ClientCollectionHandler<>(collectionBuilder, Dragon.class);
    }

    @Override
    public ClientCollectionHandler<Dragon> setup(QueueWithID<? extends MainCollectible<?>> collection) {
        QueueWithID<Dragon> dragonCollection = (QueueWithID<Dragon>) collection;
        return new ClientCollectionHandler<>(collectionBuilder, Dragon.class, dragonCollection);
    }
}
