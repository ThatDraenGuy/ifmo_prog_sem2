package Collection;

import Collection.Classes.Dragon;
import Exceptions.ElementIdException;
import cmd.CmdArgs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class CollectionHandler {
    private java.util.PriorityQueue<Dragon> collection;
    private StorageHandler storageHandler;
    private DragonBuilder dragonBuilder;

    public CollectionHandler(StorageHandler storageHandler) {
        this.collection = new java.util.PriorityQueue<>();
        this.storageHandler = storageHandler;
        this.dragonBuilder = new DragonBuilder();
    }


    public void addDragon(HashMap<Field, Object> deconstructedObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Field f : deconstructedObject.keySet()) {

            Method method = DragonBuilder.class.getMethod(f.getName(), f.getType());
            method.invoke(dragonBuilder, f.getType().cast(deconstructedObject.get(f)));
        }
        collection.add(dragonBuilder.build());
        //TODO implement + not forget about objects!!!!! + refactor into add()
    }


    public void load() {
        this.collection.addAll(this.storageHandler.load());
    }

    public void save() throws IOException {
        this.storageHandler.save(this.collection);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Dragon dragon : collection) {
            str.append(dragon.toString()).append("\n");
        }
        return str.toString();
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
