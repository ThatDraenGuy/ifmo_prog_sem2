package collection;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserAccessibleObject;
import collection.classes.Collectible;
import collection.classes.Color;
import collection.classes.DragonType;
import collection.classes.MainCollectible;
import collection.classes.builders.Builder;
import collection.classes.builders.DragonBuilder;
import exceptions.ElementIdException;
import exceptions.InvalidCollectionException;
import exceptions.ValueNotValidException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;

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
                HashMap<Field, Object> validatedObject = validate(deconstructedObject);
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
     * Method for validating and converting a deconstructed version of an object. Invokes {@link #validate(Field, String)} for each field
     *
     * @param deconstructedObject an object to be verified
     * @return validated and converted deconstructed version of an object
     * @throws ValueNotValidException if validation was failed
     */
    public HashMap<Field, Object> validate(HashMap<Field, Object> deconstructedObject) throws ValueNotValidException {
        HashMap<Field, Object> validatedObject = new HashMap<>();
        for (Field field : deconstructedObject.keySet()) {
            //TODO Collectible?
            if (field.isAnnotationPresent(UserAccessibleObject.class) && !deconstructedObject.get(field).equals("")) {
                validatedObject.put(field, validate((HashMap<Field, Object>) deconstructedObject.get(field)));
            } else {
                validatedObject.put(field, validate(field, deconstructedObject.get(field).toString()));
            }
        }
        return validatedObject;
    }

    /**
     * Validates and converts a field. Invokes {@link #convert(Field, String)}
     *
     * @param field a field that inputted value will go in
     * @param value a value to be validated
     * @return validated and converted value
     * @throws ValueNotValidException if validation was failed
     */
    public Object validate(Field field, String value) throws ValueNotValidException {
        Object convertedValue = convert(field, value);
        if (field.isAnnotationPresent(NotNull.class) && convertedValue == null) {
            throw new ValueNotValidException(field.getName() + " cannot be null!");
        }
        if (field.isAnnotationPresent(LowerBounded.class) && convertedValue != null) {
            double border = field.getAnnotation(LowerBounded.class).value();
            if (Double.parseDouble(value) <= border) {
                throw new ValueNotValidException(field.getName() + " should be greater than " + border + ". Your input: " + value);
            }
        }
        return convertedValue;
    }

    /**
     * Converts a value from String to the needed type
     *
     * @param field a field that value will go in
     * @param value a value to be converted
     * @return converted value
     * @throws ValueNotValidException if value couldn't be converted to fit the specified field
     */
    public Object convert(Field field, String value) throws ValueNotValidException {
        if (value.equals("")) return null;

        HashMap<Class<?>, Function<String, ?>> variants = new HashMap<>();
        variants.put(Long.class, Long::parseLong);
        variants.put(long.class, Long::parseLong);
        variants.put(int.class, Integer::parseInt);
        variants.put(String.class, String::valueOf);
        variants.put(double.class, Double::parseDouble);
        variants.put(java.time.ZonedDateTime.class, java.time.ZonedDateTime::parse);

        Function<String, ?> convert = variants.get(field.getType());
        if (convert != null) {
            try {
                return convert.apply(value);
            } catch (NumberFormatException | DateTimeParseException e) {
                throw new ValueNotValidException(field.getName() + " should have a " + field.getType().getSimpleName() + " value. Your value was: " + value);
            }
        }
        if (field.getType().isEnum()) {
            try {
                @SuppressWarnings({"unchecked", "rawtypes"}) Object enumValue = Enum.valueOf((Class<Enum>) field.getType(), value);
                return enumValue;
            } catch (IllegalArgumentException e) {
                throw new ValueNotValidException(field.getName() + " cannot have a value \"" + value + "\"");
            }
        }
        throw new ValueNotValidException(field.getName() + " should have a " + field.getType().getSimpleName() + " value. Your value was: " + value);

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
