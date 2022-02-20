package Collection;

import Collection.Classes.Dragon;
import Exceptions.ElementIdException;
import cmd.CmdArgs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

public class CollectionHandler {
    private java.util.PriorityQueue<Dragon> collection;
    private StorageHandler storageHandler;

    public CollectionHandler(StorageHandler storageHandler) {
        this.collection = new java.util.PriorityQueue<>();
        this.storageHandler = storageHandler;
    }

    public void add(Dragon dragon) {
        collection.add(dragon);
    }
    public Dragon convertArgs(CmdArgs args) {
        return null;
        //TODO implement
    }

    public void load() {
        this.collection.addAll(this.storageHandler.load());
    }

    public void save() throws FileNotFoundException, IOException {
        this.storageHandler.save(this.collection);
    }

    @Override
    public String toString() {
        String str = "";
        for (Dragon dragon : collection) {
            str += dragon.toString() + "\n";
        }
        return str;
    }
    public void removeFirst() throws NoSuchElementException{
            this.collection.remove();
    }
    public void removeById(String strId) throws ElementIdException {
        try {
            long id = Long.parseLong(strId);
            if (!collection.removeIf(dragon -> dragon.getId()==id)) {
                throw new ElementIdException(strId);
            }
        } catch (NumberFormatException e) {
            throw new ElementIdException(strId);
        }

    }
}
