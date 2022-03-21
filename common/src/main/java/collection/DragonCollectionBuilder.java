package collection;

import collection.classes.*;
import exceptions.InvalidCollectionException;
import exceptions.ValueNotValidException;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;

public class DragonCollectionBuilder implements CollectionBuilder<Dragon> {
    @Getter
    private final ArrayList<String> fieldNames;
    private final HashMap<String, Field> fieldsFromNames;
    private final HashMap<String, Method> methods;
    private final HashMap<String, Object> builders;
    private final RawDragon.RawDragonBuilder rawDragonBuilder;
    private final Coordinates.CoordinatesBuilder coordinatesBuilder;
    private final DragonCave.DragonCaveBuilder dragonCaveBuilder;
    private final DragonFactory factory;

    public DragonCollectionBuilder(DragonFactory factory) {
        this.factory = factory;
        rawDragonBuilder = RawDragon.builder();
        coordinatesBuilder = Coordinates.builder();
        dragonCaveBuilder = DragonCave.builder();
        fieldNames = new ArrayList<>();
        fieldsFromNames = new HashMap<>();
        methods = new HashMap<>();
        builders = new HashMap<>();
        generateMaps(RawDragon.class);
    }

    private void generateMaps(Class<?> target) {
        Field[] fields = target.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            fieldsFromNames.put(name, field);
            try {
                Method method = getBuilder(target).getClass().getMethod(name, field.getType());
                methods.put(name, method);
            } catch (NoSuchMethodException ignored) {
            }
            builders.put(name, getBuilder(target));
            if (Arrays.stream(field.getType().getInterfaces()).anyMatch(Predicate.isEqual(Collectible.class))) {
                generateMaps(field.getType());
            }
        }
    }

    private Object getBuilder(Class<?> target) {
        if (target.equals(RawDragon.class)) return rawDragonBuilder;
        else if (target.equals(Coordinates.class)) return coordinatesBuilder;
        else if (target.equals(DragonCave.class)) return dragonCaveBuilder;
        else return null;
    }

    public void put(String target, String value) throws ValueNotValidException {
        Field targetField = fieldsFromNames.get(target);
        Method targetMethod = methods.get(target);
        Object builder = builders.get(target);
        Object newValue = Validator.validate(targetField, targetField.getType(), value);
        try {
            targetMethod.invoke(builder.getClass(), newValue);
        } catch (IllegalAccessException | InvocationTargetException ignored) {

        }
    }

    //TODO think
    public void put(String target, Object value) throws ValueNotValidException {
        Field targetField = fieldsFromNames.get(target);
        Method targetMethod = methods.get(target);
        Object builder = builders.get(target);
        Object newValue = Validator.validate(targetField, value);
        System.out.println(targetField + " " + targetMethod + " " + builder + " " + newValue);
        try {
            targetMethod.invoke(builder.getClass(), newValue);
        } catch (IllegalAccessException | InvocationTargetException ignored) {

        }
    }

    private void putt(String target, Object value) throws ValueNotValidException {
        try {
            Object builder = builders.get(target);
            System.out.println(target + " " + builder);
            Field targetField = fieldsFromNames.get(target);
            System.out.println(targetField);
            Object newValue = Validator.validate(targetField, value);
            Method targetMethod = builder.getClass().getMethod(target, value.getClass());
            System.out.println(targetField + " " + targetMethod + " " + builder + " " + newValue);
            targetMethod.invoke(builder, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        }
    }

    private DragonData put(Map<String, Object> map) throws ValueNotValidException {
        Long id = null;
        ZonedDateTime creationDate = null;
        for (String key : map.keySet()) {
            if (Arrays.stream(map.get(key).getClass().getInterfaces()).anyMatch(Predicate.isEqual(Map.class)))
                put((Map<String, Object>) map.get(key));
            if (!key.equals("id") && !key.equals("creationDate")) putt(key, map.get(key));
            else if (key.equals("id")) id = (Long) map.get(key);
            else creationDate = ZonedDateTime.parse(map.get(key).toString());
        }
        return new DragonData(id, creationDate);
    }

    public RawDragon rawBuild() {
        rawDragonBuilder.cave(dragonCaveBuilder.build());
        rawDragonBuilder.coordinates(coordinatesBuilder.build());
        return rawDragonBuilder.build();
    }

    public Collection<Dragon> build(Collection<Map<String, Object>> collection) throws InvalidCollectionException {
        System.out.println(builders);
        try {
            Collection<Dragon> resultCollection = new ArrayList<>();
            for (Map<String, Object> map : collection) {
                DragonData data = put(map);
                resultCollection.add(build(data));
            }
            return resultCollection;
        } catch (ValueNotValidException e) {
            throw new InvalidCollectionException();
        }
    }

    private Dragon build(DragonData data) {
        RawDragon rawDragon = rawBuild();
        return factory.getObject(rawDragon, data.getId(), data.getCreationDate());
    }

    private class DragonData {
        @Getter
        private Long id;
        @Getter
        private ZonedDateTime creationDate;

        public DragonData(Long id, ZonedDateTime creationDate) {
            this.id = id;
            this.creationDate = creationDate;
        }
    }
}
