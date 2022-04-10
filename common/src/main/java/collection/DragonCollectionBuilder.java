package collection;

import annotations.CollectibleField;
import collection.classes.*;
import exceptions.InvalidCollectionException;
import exceptions.ValueNotValidException;
import lombok.Getter;
import utility.ArrayListWithID;
import utility.CollectionWithID;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;

public class DragonCollectionBuilder implements CollectionBuilder<Dragon> {
    @Getter
    private final HashMap<String, Field> fieldsFromNames;
    private final DragonFactory factory;
    private final List<Field> dragonFields;
    private final List<Field> rawDragonFields;
    private final List<Field> coordinatesFields;
    private final List<Field> dragonCaveFields;

    public DragonCollectionBuilder(DragonFactory factory) {
        this.factory = factory;
//        fieldNames = new ArrayList<>();
        fieldsFromNames = new HashMap<>();

        dragonFields = List.of(Dragon.class.getDeclaredFields());
        rawDragonFields = List.of(RawDragon.class.getDeclaredFields());
        coordinatesFields = List.of(Coordinates.class.getDeclaredFields());
        dragonCaveFields = List.of(DragonCave.class.getDeclaredFields());

        generateMaps(Dragon.class);
    }

    private void generateMaps(Class<?> target) {
        Field[] fields = target.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            fieldsFromNames.put(name, field);
            if (Arrays.stream(field.getType().getInterfaces()).anyMatch(Predicate.isEqual(collection.classes.Collectible.class))) {
                generateMaps(field.getType());
            }
        }
    }


    public List<Field> getClassFields(Class<?> target) {
        if (target.equals(Dragon.class)) return dragonFields;
        else if (target.equals(RawDragon.class)) return rawDragonFields;
        else if (target.equals(Coordinates.class)) return coordinatesFields;
        else if (target.equals(DragonCave.class)) return dragonCaveFields;
        else return null;
    }

    public RawDragon rawBuild(Map<String, Object> map) throws ValueNotValidException {
        Map<String, Object> newMap = fullConvert(map, RawDragon.class);
        RawDragon.RawDragonBuilder builder = RawDragon.builder();
        builder.age((Long) newMap.get("age"));
        builder.character((DragonCharacter) newMap.get("character"));
        builder.type((DragonType) newMap.get("type"));
        builder.color((Color) newMap.get("color"));
        builder.name((String) newMap.get("name"));
        builder.coordinates(buildCoords(newMap.get("coordinates")));
        builder.cave(buildCave(newMap.get("cave")));
        return builder.build();
    }

    public CollectionWithID<Dragon> build(CollectionWithID<Map<String, Object>> collection) throws InvalidCollectionException {
        long id = collection.getId();
        try {
            CollectionWithID<Dragon> resultCollection = new ArrayListWithID<>(id);
            for (Map<String, Object> map : collection) {
                resultCollection.add(buildDragon(map));
            }
            return resultCollection;
        } catch (ValueNotValidException e) {
            throw new InvalidCollectionException(e.toString());
        }
    }

    private Dragon buildDragon(Map<String, Object> map) throws ValueNotValidException {
        Map<String, Object> newMap = fullConvert(map, RawDragon.class);
        RawDragon rawDragon = rawBuild(map);
        long id = (long) newMap.get("id");
        ZonedDateTime creationDate = (ZonedDateTime) newMap.get("creationDate");
        return factory.getObject(rawDragon, id, creationDate);
    }

    private Coordinates buildCoords(Object object) {
        @SuppressWarnings({"unchecked"})
        Map<String, Object> map = (Map<String, Object>) object;
        Coordinates.CoordinatesBuilder builder = Coordinates.builder();
        builder.x((Integer) map.get("x"));
        builder.y((Long) map.get("y"));
        return builder.build();
    }

    private DragonCave buildCave(Object object) {
        @SuppressWarnings({"unchecked"})
        Map<String, Object> map = (Map<String, Object>) object;
        DragonCave.DragonCaveBuilder builder = DragonCave.builder();
        builder.depth((Integer) map.get("depth"));
        return builder.build();
    }

    private Map<String, Object> fullConvert(Map<String, Object> map, Class<?> target) throws ValueNotValidException {
        addMissingValues(map, target);
        Map<Field, Object> fieldStringMap = convertMapToFields(map);
        Map<Field, Object> stringMap = convertMapToString(fieldStringMap);

        Map<Field, Object> finalMap = Validator.convertAndValidate(stringMap);
        Map<String, Object> newFinalMap = new HashMap<>();
        for (Field field : finalMap.keySet()) {
            newFinalMap.put(field.getName(), finalMap.get(field));
        }
        return newFinalMap;
    }

    private Map<Field, Object> convertMapToFields(Map<String, Object> map) {
        Map<Field, Object> newMap = new HashMap<>();
        for (String key : fieldsFromNames.keySet()) {
            if (map.containsKey(key)) newMap.put(fieldsFromNames.get(key), map.get(key));
        }
        return newMap;
    }

    private void addMissingValues(Map<String, Object> map, Class<?> target) {
//        System.out.println("miss: " + map + " " + target);
        List<Field> fields = getClassFields(target);
        for (Field field : fields) {
            if (!map.containsKey(field.getName())) map.put(field.getName(), null);
        }
//        System.out.println("miss: " + map + " " + target);
    }

    private Map<Field, Object> convertMapToString(Map<Field, Object> map) throws ValueNotValidException {
        Map<Field, Object> newMap = new HashMap<>();
        for (Field field : map.keySet()) {
            if (!field.isAnnotationPresent(CollectibleField.class)) {
                newMap.put(field, convertValueToString(map.get(field)));
            } else {
//                System.out.println("convert: " + field + " " + map.get(field));
                @SuppressWarnings({"unchecked"})
                Map<String, Object> castedMap = (Map<String, Object>) map.get(field);
                newMap.put(field, fullConvert(castedMap, field.getType()));
            }
        }
        return newMap;
    }

    private String convertValueToString(Object o) {
        if (o == null) return "";
        else return o.toString();
    }
}
