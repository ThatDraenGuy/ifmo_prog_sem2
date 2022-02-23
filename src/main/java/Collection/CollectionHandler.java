package Collection;

import Collection.Classes.Color;
import Collection.Classes.Dragon;
import Collection.Classes.DragonType;
import Exceptions.ElementIdException;
import Exceptions.InvalidCollectionException;
import Exceptions.ValueNotValidException;
import Annotations.LowerBounded;
import Annotations.NotNull;
import Annotations.UserAccessibleObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class CollectionHandler {
    private java.util.PriorityQueue<Dragon> collection;
    private StorageHandler storageHandler;
    private DragonBuilder dragonBuilder;
    private Class<?> targetClass;

    public CollectionHandler(StorageHandler storageHandler, Class<?> targetClass) {
        this.collection = new java.util.PriorityQueue<>();
        this.storageHandler = storageHandler;
        this.targetClass=targetClass;
    }


    public void add(HashMap<Field, Object> deconstructedObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        collection.add(constructDragon(deconstructedObject));
    }
    public void update(String arg, HashMap<Field, Object> deconstructedObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ElementIdException {
        Dragon newObject = constructDragon(deconstructedObject);
        Long argLong = Long.parseLong(arg);
        newObject.setId(argLong);
        boolean result = collection.removeIf(dragon -> dragon.getId().equals(argLong));
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
                if (deconstructedObject.get(f).equals("")) {
                    method.invoke(dragonBuilder, null);
                } else {
                    method.invoke(dragonBuilder, (HashMap<Field, Object>) deconstructedObject.get(f));
                }
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
    public void clear() {
        collection.clear();
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
    public void removeLower(HashMap<Field, Object> deconstructedObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Dragon target = constructDragon(deconstructedObject);
        collection.removeIf(dragon -> dragon.compareTo(target)>0);
    }
    public int countByColor(String arg) throws IllegalArgumentException{
        Color target = Color.valueOf(arg);
        //TODO normal validation?
        int res=0;
        for (Dragon dragon : collection) {
            if (dragon.getColor().equals(target)) {
                res++;
            }
        }
        return res;
    }
    public String filterByType(String arg) throws IllegalArgumentException {
        DragonType type = DragonType.valueOf(arg);
        StringBuilder str = new StringBuilder();
        for (Dragon dragon : collection) {
            if (dragon.getType().equals(type)) {
                str.append(dragon).append("\n");
            }
        }
        return str.toString();
    }
    public String filterGreaterThanAge(String arg) {
        long age = Long.parseLong(arg);
        StringBuilder str = new StringBuilder();
        for (Dragon dragon : collection) {
            if (dragon.getAge()>age) {
                str.append(dragon).append("\n");
            }
        }
        return str.toString();
    }
    public String info() {
        return "This collection's type is a "+collection.getClass().getName()+", it contains "+collection.size()+" elements.";
        //TODO init date?
    }
    public void validate(Field field, String value) throws ValueNotValidException {
        if (field.isAnnotationPresent(NotNull.class) && value.equals("")) {
            throw new ValueNotValidException(field.getName()+" cannot be null!");
        }
        if (field.isAnnotationPresent(LowerBounded.class) && value!="") {
            double border = field.getAnnotation(LowerBounded.class).value();
            if (Double.parseDouble(value)<=border) {
                throw new ValueNotValidException(field.getName()+" should be greater than "+Double.doubleToLongBits(border)+". Your input: "+value);
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
    public Class<?> getTargetClass() {
        return targetClass;
    }
}
