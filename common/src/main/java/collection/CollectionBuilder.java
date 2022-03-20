package collection;

import collection.classes.Collectible;
import collection.classes.Coordinates;
import collection.classes.DragonCave;
import collection.classes.RawDragon;
import exceptions.ValueNotValidException;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;

public class CollectionBuilder {
    @Getter
    private final ArrayList<String> fieldNames;
    private final HashMap<String, Field> fieldsFromNames;
    private final HashMap<String, Method> methods;
    private final HashMap<String, Object> builders;
    private final RawDragon.RawDragonBuilder rawDragonBuilder;
    private final Coordinates.CoordinatesBuilder coordinatesBuilder;
    private final DragonCave.DragonCaveBuilder dragonCaveBuilder;

    public CollectionBuilder() {
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
            if (Arrays.stream(field.getType().getInterfaces()).anyMatch(Predicate.isEqual(Collectible.class))) {
                generateMaps(field.getType());
            } else {
                fieldsFromNames.put(name, field);
                try {
                    Method method = rawDragonBuilder.getClass().getMethod(name, field.getType());
                    methods.put(name, method);
                } catch (NoSuchMethodException ignored) {
                }
                builders.put(name, getBuilder(target));
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
        try {
            targetMethod.invoke(builder.getClass(), Validator.validate(targetField, targetField.getType(), value));
        } catch (IllegalAccessException | InvocationTargetException ignored) {

        }
    }

    public RawDragon build() {
        rawDragonBuilder.cave(dragonCaveBuilder.build());
        rawDragonBuilder.coordinates(coordinatesBuilder.build());
        return rawDragonBuilder.build();
    }
}
