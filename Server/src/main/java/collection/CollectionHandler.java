package collection;

import annotations.UserAccessibleObject;
import collection.builders.Builder;
import collection.builders.DragonBuilder;
import collection.classes.Collectible;
import collection.classes.Color;
import collection.classes.DragonType;
import collection.classes.MainCollectible;
import exceptions.ElementIdException;
import exceptions.InvalidCollectionException;
import exceptions.ValueNotValidException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * A class that manages a collection. Has methods for all interactions with it and also handles collection's construction/deconstruction.
 */
public class CollectionHandler {
    final private Class<? extends MainCollectible<?>> targetClass;
    final private PriorityQueue<MainCollectible> collection;
    final private StorageHandler storageHandler;
    private DragonBuilder builder;

    public CollectionHandler(StorageHandler storageHandler, Class<? extends MainCollectible<?>> target) {
        this.collection = new PriorityQueue<>();
        this.storageHandler = storageHandler;
        this.builder = new DragonBuilder();
        this.targetClass = target;
    }

    /**
     * A method for adding an object to collection
     *
     * @param deconstructedObject a deconstructed version of an object to be added
     */
    public void add(HashMap<Field, Object> deconstructedObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        collection.add(constructObject(deconstructedObject, targetClass, builder));
    }

    /**
     * A method for updating an element in the collection
     *
     * @param arg                 id of an object to be replaced
     * @param deconstructedObject a deconstructed version of a new object
     * @throws ElementIdException if there is no element with the specified id.
     */
    public void update(String arg, HashMap<Field, Object> deconstructedObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ElementIdException, NoSuchFieldException {
        Long argLong = Long.parseLong(arg);
        Field id = targetClass.getDeclaredField("id");
        deconstructedObject.put(id, argLong);
        MainCollectible<?> newObject = constructObject(deconstructedObject, targetClass, builder);
        boolean result = collection.removeIf(dragon -> dragon.getId().equals(argLong));
        if (result) {
            collection.add(newObject);
        } else {
            throw new ElementIdException(arg);
        }
    }

    /**
     * A method to construct an object from HashMap form.
     *
     * @param deconstructedObject deconstructed version of an object
     * @param builder             object's builder
     * @param <T>                 class of an object
     * @return constructed version of an object
     */
    public <T extends Collectible> T constructObject(HashMap<Field, Object> deconstructedObject, Class<T> target, Builder builder) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        for (Field f : deconstructedObject.keySet()) {
            if (f.isAnnotationPresent(UserAccessibleObject.class) && deconstructedObject.get(f) != null) {
                builder.put(f, constructObject((HashMap<Field, Object>) deconstructedObject.get(f), (Class<Collectible>) f.getType()));
            } else {
                builder.put(f, deconstructedObject.get(f));
            }
        }
        return builder.build();
    }

    public <T extends Collectible> T constructObject(HashMap<Field, Object> deconstructedObject, Class<T> target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Builder builder = (Builder) target.getDeclaredMethod("getBuilder", null).invoke(null, null);
        return constructObject(deconstructedObject, target, builder);
    }

    /**
     * A method to deconstruct object into the HashMap form. Used when saving collection
     *
     * @param object object to be deconstructed
     * @param target class of an object
     * @return deconstructed object
     */
    public HashMap<Field, Object> deconstructObject(Object object, Class<?> target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        HashMap<Field, Object> deconstructedObject = new HashMap<>();
        Field[] fields = target.getDeclaredFields();
        for (Field field : fields) {
            if (!field.getType().equals(Builder.class)) {
                String name = field.getName();
                String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = target.getMethod(methodName, null);
                Object obj = method.invoke(object, null);
                //TODO maybe check Collectible instead?
                if (field.isAnnotationPresent(UserAccessibleObject.class)) {
                    if (obj == null) {
                        deconstructedObject.put(field, null);
                    } else {
                        deconstructedObject.put(field, deconstructObject(obj, field.getType()));
                    }
                } else {
                    deconstructedObject.put(field, obj);
                }
            }
        }
        return deconstructedObject;
    }

    /**
     * A method to load collection from a file. Invokes {@link StorageHandler#load(Class)} and constructs collection. Afterwards it
     * invokes {@link #checkIds(PriorityQueue)}.
     */
    public void load() {
        try {
            ArrayList<HashMap<Field, Object>> deconstructedCollection = this.storageHandler.load(targetClass);
            PriorityQueue<MainCollectible> loadedCollection = new PriorityQueue<>();
            for (HashMap<Field, Object> deconstructedObject : deconstructedCollection) {
                HashMap<Field, Object> validatedObject = Validator.validate(deconstructedObject);
                loadedCollection.add(constructObject(validatedObject, targetClass, builder));
            }
            long id = checkIds(loadedCollection);
            this.collection.addAll(loadedCollection);
            builder = new DragonBuilder(id + 1);

        } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException e) {
            System.out.println(e);
        } catch (InvalidCollectionException | ValueNotValidException e) {
            System.out.println("Loaded collection is invalid, initializing an empty collection...");
        }
    }

    /**
     * A method for saving collection in a file. Invokes {@link StorageHandler#save(ArrayList, Class)}
     *
     * @throws IOException if there was an exception writing info in file
     */
    public void save() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ArrayList<HashMap<Field, Object>> deconstructedCollection = new ArrayList<>();
        for (MainCollectible collectable : collection) {
            deconstructedCollection.add(deconstructObject(collectable, targetClass));
        }
        storageHandler.save(deconstructedCollection, targetClass);
    }

    /**
     * Clears collection
     */
    public void clear() {
        collection.clear();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (MainCollectible<?> dragon : collection) {
            str.append(dragon.toString()).append("\n");
        }
        return str.toString();
    }

    /**
     * Removes first element from collection
     */
    public void removeFirst() throws NoSuchElementException {
        this.collection.remove();
    }

    /**
     * Removes element with a specified id from collection.
     *
     * @param strId id of element to be removed
     * @throws ElementIdException if there is no element with such id
     */
    public void removeById(String strId) throws ElementIdException {
        try {
            long id = Long.parseLong(strId);
            if (!collection.removeIf(dragon -> dragon.getId() == id)) {
                throw new ElementIdException(strId);
            }
        } catch (NumberFormatException e) {
            throw new ElementIdException(strId);
        }

    }

    /**
     * Removes all elements that are lower than specified from collection
     *
     * @param deconstructedObject deconstructed version of the "target" element
     */
    public void removeLower(HashMap<Field, Object> deconstructedObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        MainCollectible<?> target = constructObject(deconstructedObject, targetClass, builder);
        collection.removeIf(dragon -> dragon.compareTo(target) < 0);
    }

    /**
     * Returns the number of elements that have the specified color.
     *
     * @param arg a color
     * @return number of elements with that color
     * @throws IllegalArgumentException if there is no such color
     */
    public int countByColor(String arg) throws IllegalArgumentException {
        Color target = Color.valueOf(arg);
        int res = 0;
        for (MainCollectible<?> dragon : collection) {
            if (dragon.getColor().equals(target)) {
                res++;
            }
        }
        return res;
    }

    /**
     * Filters only elements with the specified type
     *
     * @param arg a type
     * @return result of #toString of all filtered elements
     * @throws IllegalArgumentException if there is no such type
     */
    public String filterByType(String arg) throws IllegalArgumentException {
        DragonType type = DragonType.valueOf(arg);
        StringBuilder str = new StringBuilder();
        for (MainCollectible<?> dragon : collection) {
            DragonType collectibleType = dragon.getType();
            if (collectibleType != null && collectibleType.equals(type)) {
                str.append(dragon).append("\n");
            }
        }
        return str.toString();
    }

    /**
     * Filters only elements with age greater than specified
     *
     * @param arg an age
     * @return result of #toString of all filtered elements
     */
    public String filterGreaterThanAge(String arg) {
        long age = Long.parseLong(arg);
        StringBuilder str = new StringBuilder();
        for (MainCollectible<?> dragon : collection) {
            Long collectibleAge = dragon.getAge();
            if (collectibleAge != null && collectibleAge > age) {
                str.append(dragon).append("\n");
            }
        }
        return str.toString();
    }

    /**
     * @return information about collection
     */
    public String info() {
        return "This collection's type is a " + collection.getClass().getName() + ", it contains " + collection.size() + " elements.";
        //TODO init date?
    }



    /**
     * Checks ids of collection to make sure there aren't any duplicates
     *
     * @param collection collection which ids need to be checked
     * @return the highest id
     * @throws InvalidCollectionException if there were duplicate ids
     */
    public Long checkIds(PriorityQueue<MainCollectible> collection) throws InvalidCollectionException {
        HashSet<Long> ids = new HashSet<>();
        for (MainCollectible<?> dragon : collection) {
            boolean res = ids.add(dragon.getId());
            if (!res) {
                throw new InvalidCollectionException();
            }
        }
        long highestId = 0L;
        for (long id : ids) {
            if (id > highestId) {
                highestId = id;
            }
        }
        return highestId;
    }

    public Class<? extends MainCollectible> getTargetClass() {
        return targetClass;
    }
}
