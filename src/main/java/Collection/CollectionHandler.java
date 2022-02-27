package Collection;

import Collection.Classes.*;
import Collection.Classes.Builders.Builder;
import Collection.Classes.Builders.DragonBuilder;
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
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class CollectionHandler {
    private Class<? extends MainCollectible<?>> targetClass = Dragon.class;
    private java.util.PriorityQueue<MainCollectible> collection;
    private StorageHandler storageHandler;
    private DragonBuilder dragonBuilder;

    public CollectionHandler(StorageHandler storageHandler) {
        this.collection = new java.util.PriorityQueue<>();
        this.storageHandler = storageHandler;
    }


    public void add(HashMap<Field, Object> deconstructedObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        collection.add(constructObject(deconstructedObject, targetClass));
    }
    public void update(String arg, HashMap<Field, Object> deconstructedObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ElementIdException, NoSuchFieldException {
        MainCollectible<?> newObject = constructObject(deconstructedObject, targetClass);
        Long argLong = Long.parseLong(arg);
        newObject.setId(argLong);
        boolean result = collection.removeIf(dragon -> dragon.getId().equals(argLong));
        if (result) {
            collection.add(newObject);
        } else {
            throw new ElementIdException(arg);
        }
    }
    public <T extends Collectible> T constructObject(HashMap<Field,Object> deconstructedObject, Class<T> target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Builder builder = (Builder) target.getDeclaredMethod("getBuilder", null).invoke(null, null);
        for (Field f : deconstructedObject.keySet()) {
            if (f.isAnnotationPresent(UserAccessibleObject.class)) {
                builder.put(f, constructObject((HashMap<Field, Object>) deconstructedObject.get(f), (Class<Collectible>) f.getType()));
            } else {
                builder.put(f, deconstructedObject.get(f));
            }
            //TODO think
        }
        return builder.build();
    }


    public void load() {
        java.util.PriorityQueue<MainCollectible> loadedCollection = this.storageHandler.load(targetClass);
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
        for (MainCollectible<?> dragon : collection) {
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
    public void removeLower(HashMap<Field, Object> deconstructedObject) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        MainCollectible<?> target = constructObject(deconstructedObject, targetClass);
        collection.removeIf(dragon -> dragon.compareTo(target)>0);
        //TODO fix?
    }
    public int countByColor(String arg) throws IllegalArgumentException{
        Color target = Color.valueOf(arg);
        //TODO normal validation?
        int res=0;
        for (MainCollectible<?> dragon : collection) {
            if (dragon.getColor().equals(target)) {
                res++;
            }
        }
        return res;
    }
    public String filterByType(String arg) throws IllegalArgumentException {
        DragonType type = DragonType.valueOf(arg);
        StringBuilder str = new StringBuilder();
        for (MainCollectible<?> dragon : collection) {
            if (dragon.getType().equals(type)) {
                str.append(dragon).append("\n");
            }
        }
        return str.toString();
    }
    public String filterGreaterThanAge(String arg) {
        long age = Long.parseLong(arg);
        StringBuilder str = new StringBuilder();
        for (MainCollectible<?> dragon : collection) {
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
    public Object validate(Field field, String value) throws ValueNotValidException {
        Object convertedValue = convert(field, value);
        if (field.isAnnotationPresent(NotNull.class) && convertedValue==null) {
            throw new ValueNotValidException(field.getName()+" cannot be null!");
        }
        if (field.isAnnotationPresent(LowerBounded.class) && convertedValue!=null) {
            double border = field.getAnnotation(LowerBounded.class).value();
            if (Double.parseDouble(value)<=border) {
                throw new ValueNotValidException(field.getName()+" should be greater than "+border+". Your input: "+value);
                //TODO fix longValue()
            }
        }
        return convertedValue;
    }
    public Object convert(Field field, String value) throws ValueNotValidException {
        if (value.equals("")) return null;

        HashMap<Class<?>, Function<String,?>> variants = new HashMap<>();
        variants.put(Long.class, Long::parseLong);
        variants.put(long.class, Long::parseLong);
        variants.put(int.class, Integer::parseInt);
        variants.put(String.class, String::valueOf);
        variants.put(double.class, Double::parseDouble);
        variants.put(java.time.ZonedDateTime.class, java.time.ZonedDateTime::parse);

        Function<String,?> convert = variants.get(field.getType());
        if (convert!=null) {
            try {
                return convert.apply(value);
            } catch (NumberFormatException | DateTimeParseException e) {
                throw new ValueNotValidException(field.getName()+" should have a "+field.getType().getSimpleName()+" value. Your value was: "+value);
                //TODO message
            }
        }
        if (field.getType().isEnum()) {
            try {
                @SuppressWarnings({"unchecked", "rawtypes"}) Object enumValue = Enum.valueOf((Class<Enum>) field.getType(), value);
                return enumValue;
            } catch (IllegalArgumentException e) {
                throw new ValueNotValidException(field.getName()+" cannot have a value \""+value+"\"");
            }
        }
        throw new ValueNotValidException(field.getName()+" should have a "+field.getType().getSimpleName()+" value. Your value was: "+value);

    }
    public Long checkIds(java.util.PriorityQueue<MainCollectible> collection) throws InvalidCollectionException {
        HashSet<Long> ids = new HashSet<>();
        for (MainCollectible<?> dragon : collection) {
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
