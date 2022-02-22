package Collection;

import Collection.Classes.Dragon;
import Exceptions.ElementIdException;
import Exceptions.InvalidCollectionException;
import Exceptions.ValueNotValidException;
import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserAccessibleObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class CollectionHandler {
    private java.util.PriorityQueue<Dragon> collection;
    private StorageHandler storageHandler;
    private DragonBuilder dragonBuilder;
    private Class targetClass;

    public CollectionHandler(StorageHandler storageHandler, Class targetClass) {
        this.collection = new java.util.PriorityQueue<>();
        this.storageHandler = storageHandler;
        this.targetClass=targetClass;
    }


    public void add(HashMap<Field, Object> deconstructedObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        collection.add(constructDragon(deconstructedObject));
        //TODO implement + not forget about objects!!!!! + refactor into add()
    }
    public void update(String arg, HashMap<Field, Object> deconstructedObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ElementIdException {
        Dragon newObject = constructDragon(deconstructedObject);
        Long argLong = Long.parseLong(arg);
        newObject.setId(argLong);
        boolean result = collection.removeIf(dragon -> dragon.getId()==argLong);
        if (result) {
            collection.add(newObject);
        } else {
            throw new ElementIdException(arg);
        }
    }
    public Dragon constructDragon(HashMap<Field,Object> deconstructedObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Field f : deconstructedObject.keySet()) {
            Method method;
            if (f.isAnnotationPresent(UserAccessibleObject.class)) {
                method = DragonBuilder.class.getMethod(f.getName(), HashMap.class);
                HashMap<Field,Object> obj = (HashMap<Field, Object>) deconstructedObject.get(f);
                method.invoke(dragonBuilder, obj);
            } else {
                method = DragonBuilder.class.getMethod(f.getName(), String.class);
                method.invoke(dragonBuilder, deconstructedObject.get(f).toString());
            }
        }
        return dragonBuilder.build();
    }


    public void load() {
        java.util.PriorityQueue<Dragon> loadedCollection = this.storageHandler.load();
        try {
            long id = checkIds(loadedCollection);
            this.collection.addAll(loadedCollection);
            dragonBuilder = new DragonBuilder(id+1);
        } catch (InvalidCollectionException e) {
            System.out.println("Loaded collection is invalid, initializing an empty collection...");
        }
        //TODO system.out??
    }

    public void save() throws IOException {
        //TODO think about giving this up
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
    public void validate(Field field, String value) throws ValueNotValidException {
        if (field.isAnnotationPresent(NotNull.class) && value=="") {
            throw new ValueNotValidException(field.getName()+" cannot be null!");
        }
        if (field.isAnnotationPresent(LowerBounded.class) && value!="") {
            Double border = field.getAnnotation(LowerBounded.class).value();
            if (Double.parseDouble(value)<=border) {
                throw new ValueNotValidException(field.getName()+" should be greater than "+border.longValue()+". Your input: "+value);
                //TODO fix longValue()
            }
        }
        if (field.getType().isEnum() && value!="") {
            try {
                @SuppressWarnings({"unchecked", "rawtypes"}) Object enumConstant = Enum.valueOf((Class<Enum>) field.getType(), value);
            } catch (IllegalArgumentException e) {
                throw new ValueNotValidException(field.getName()+" cannot have a value \""+value+"\"");
            }
        }
    }
    public Long checkIds(java.util.PriorityQueue<Dragon> collection) throws InvalidCollectionException {
        HashSet<Long> ids = new HashSet<>();
        for (Dragon dragon : collection) {
            boolean res = ids.add(dragon.getId());
            if (!res) {
                throw new InvalidCollectionException();
            }
        }
        long highestId=0L;
        for (long id : ids) {
            if (id>highestId) {
                highestId=id;
            }
        }
        return highestId;
    }
    public Class getTargetClass() {
        return targetClass;
    }
}
