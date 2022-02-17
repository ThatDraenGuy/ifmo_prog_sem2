package Collection;

import Collection.Classes.Dragon;

public class CollectionHandler {
    private java.util.Vector<Dragon> collection;
    private StorageHandler storageHandler;
    public  CollectionHandler(StorageHandler storageHandler) {
        this.collection = new java.util.Vector<>();
        this.storageHandler=storageHandler;
    }
    public void add(Dragon labWork) {
        collection.add(labWork);
    }
    public void load() {
        this.collection.addAll(this.storageHandler.load());
    }
}
