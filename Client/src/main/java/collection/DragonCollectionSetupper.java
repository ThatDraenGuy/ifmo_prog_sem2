package collection;

import collection.classes.Dragon;
import collection.classes.MainCollectible;
import utility.ListAndId;

public class DragonCollectionSetupper implements CollectionSetupper<Dragon> {

    public DragonCollectionSetupper() {
    }

    @Override
    public ClientCollectionHandler<Dragon> setup() {
        return new ClientCollectionHandler<>(Dragon.class);
    }

    @Override
    public ClientCollectionHandler<Dragon> setup(ListAndId<? extends MainCollectible<?>> collection) {
        @SuppressWarnings({"unchecked"})
        ListAndId<Dragon> dragonCollection = (ListAndId<Dragon>) collection;
        return new ClientCollectionHandler<>(Dragon.class, dragonCollection);
    }
}
