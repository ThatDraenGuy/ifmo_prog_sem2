package collection;

import collection.classes.Dragon;
import collection.classes.DragonFactory;
import collection.classes.MainCollectible;
import utility.QueueWithID;

public class DragonCollectionSetupper implements CollectionSetupper<Dragon> {

    public DragonCollectionSetupper() {
    }

    @Override
    public ClientCollectionHandler<Dragon> setup() {
        return new ClientCollectionHandler<>(Dragon.class);
    }

    @Override
    public ClientCollectionHandler<Dragon> setup(QueueWithID<? extends MainCollectible<?>> collection) {
        @SuppressWarnings({"unchecked"})
        QueueWithID<Dragon> dragonCollection = (QueueWithID<Dragon>) collection;
        return new ClientCollectionHandler<>(Dragon.class, dragonCollection);
    }
}
