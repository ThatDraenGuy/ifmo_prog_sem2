package Collection;

import Collection.Classes.Dragon;

public class CollectionHandler {
    private java.util.PriorityQueue<Dragon> collection;
    private StorageHandler storageHandler;
    public  CollectionHandler(StorageHandler storageHandler) {
        this.collection = new java.util.PriorityQueue<>();
        this.storageHandler=storageHandler;
    }
    public void add(Dragon labWork) {
        collection.add(labWork);
    }
    public void load() {
        this.collection.addAll(this.storageHandler.load());
    }
    public void save() {
        this.storageHandler.save(this.collection);
    }
}
