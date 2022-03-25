package collection;

import annotations.CollectibleField;
import collection.classes.*;
import exceptions.InvalidCollectionException;
import exceptions.ValueNotValidException;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;

public class DragonCollectionBuilder implements CollectionBuilder<Dragon> {
    @Getter
    private final ArrayList<String> fieldNames;
    @Getter
    private final HashMap<String, Field> fieldsFromNames;
    private final HashMap<String, Method> methods;
    private final HashMap<String, Object> builders;
    private final RawDragon.RawDragonBuilder rawDragonBuilder;
    private final Coordinates.CoordinatesBuilder coordinatesBuilder;
    private final DragonCave.DragonCaveBuilder dragonCaveBuilder;
    private final DragonFactory factory;
    private final List<Field> dragonFields;
    private final List<Field> coordinatesFields;
    private final List<Field> dragonCaveFields;

    public DragonCollectionBuilder(DragonFactory factory) {
        this.factory = factory;
        rawDragonBuilder = RawDragon.builder();
        coordinatesBuilder = Coordinates.builder();
        dragonCaveBuilder = DragonCave.builder();
        fieldNames = new ArrayList<>();
        fieldsFromNames = new HashMap<>();
        methods = new HashMap<>();
        builders = new HashMap<>();
        dragonFields = List.of(Dragon.class.getDeclaredFields());
        coordinatesFields = List.of(Coordinates.class.getDeclaredFields());
        dragonCaveFields = List.of(DragonCave.class.getDeclaredFields());

        generateMaps(Dragon.class);
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
            if (Arrays.stream(field.getType().getInterfaces()).anyMatch(Predicate.isEqual(collection.classes.Collectible.class))) {
                generateMaps(field.getType());
            }
        }
    }

    private Object getBuilder(Class<?> target) {
        if (target.equals(RawDragon.class) | target.equals(Dragon.class)) return rawDragonBuilder;
        else if (target.equals(Coordinates.class)) return coordinatesBuilder;
        else if (target.equals(DragonCave.class)) return dragonCaveBuilder;
        else return null;
    }

    public List<Field> getClassFields(Class<?> target) {
        if (target.equals(RawDragon.class) | target.equals(Dragon.class)) return dragonFields;
        else if (target.equals(Coordinates.class)) return coordinatesFields;
        else if (target.equals(DragonCave.class)) return dragonCaveFields;
        else return null;
    }


    //    public void put(String target, String value) throws ValueNotValidException {
//        Field targetField = fieldsFromNames.get(target);
//        Method targetMethod = methods.get(target);
//        Object builder = builders.get(target);
//        Object newValue = Validator.validate(targetField, targetField.getType(), value);
//        try {
//            targetMethod.invoke(builder.getClass(), newValue);
//        } catch (IllegalAccessException | InvocationTargetException ignored) {
//
//        }
//    }
//
//    //TODO think
//    public void put(String target, Object value) throws ValueNotValidException {
//        Field targetField = fieldsFromNames.get(target);
//        Method targetMethod = methods.get(target);
//        Object builder = builders.get(target);
//        Object newValue = Validator.validate(targetField, value);
//        System.out.println(targetField + " " + targetMethod + " " + builder + " " + newValue);
//        try {
//            targetMethod.invoke(builder.getClass(), newValue);
//        } catch (IllegalAccessException | InvocationTargetException ignored) {
//
//        }
//    }

//    private void put(String target, Object value, Object builder, Class<?> type) {
//        try {
//            System.out.println(target+" "+value);
//            Method targetMethod = builder.getClass().getMethod(target, type);
//            targetMethod.invoke(builder, value);
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
//            System.out.println(ignored);
//        }
//    }

//    private DragonData put(Map<String, Object> map, List<Field> target, Object builder) throws ValueNotValidException {
//        Long id = null;
//        ZonedDateTime creationDate = null;
//        for (Field field : target) {
//            String name = field.getName();
//            Object value = map.get(name);
//            if (field.isAnnotationPresent(UserAccessibleObject.class)) {
//                if (name.equals("coordinates")) {
//                    put((Map<String, Object>) value, coordinatesFields, coordinatesBuilder);
//                } else {
//                    put((Map<String, Object>) value, dragonCaveFields, dragonCaveBuilder);
//                }
//            } else {
//                Object newValue;
//                if (value==null) {
//                    Validator.validate(field, value);
//                    newValue = value;
//                }
//                else {
//                    newValue = Validator.validate(field, value.toString());
//                }
//                if (name.equals("id")) id = Long.parseLong(newValue.toString());
//                else if (name.equals("creationDate")) creationDate = ZonedDateTime.parse((String) newValue);
//                else put(name, newValue, builder, field.getType());
//            }
//        }
//        return new DragonData(id, creationDate);
//
//
////        Long id = null;
////        ZonedDateTime creationDate = null;
////        Map<Field, Object> checkingMap = new HashMap<>();
////        for (String key : map.keySet()) {
////            if (Arrays.stream(map.get(key).getClass().getInterfaces()).anyMatch(Predicate.isEqual(Map.class)))
////                put((Map<String, Object>) map.get(key));
////            else if (key.equals("id")) id = (Long) map.get(key);
////            else if (key.equals("creationDate")) creationDate  = ZonedDateTime.parse(map.get(key).toString());
////            else {
////                checkingMap.put(fieldsFromNames.get(key), map.get(key));
////                putt(key, map.get(key));
////            }
//////            if (!key.equals("id") && !key.equals("creationDate")) putt(key, map.get(key));
//////            else if (key.equals("id")) id = (Long) map.get(key);
//////            else creationDate = ZonedDateTime.parse(map.get(key).toString());
////        }
////        System.out.println(checkingMap);
////        Validator.validate(checkingMap);
////        return new DragonData(id, creationDate);
//    }

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
        RawDragon rawDragon = builder.build();
        return rawDragon;
    }


    //    private Dragon build(Map<String, Object> map) throws ValueNotValidException {
//        for (String key : map.keySet()) {
//            if (fieldNames.contains(key)) {
//                Object object = map.get(key);
//                switch (key) {
//                    case "name": rawDragonBuilder.name((String) object); break;
//                    case ""
//                }
//            } else {
//                throw new ValueNotValidException("No such field: "+key);
//            }
//        }
//    }
    public Collection<Dragon> build(Collection<Map<String, Object>> collection) throws InvalidCollectionException {
        try {
            Collection<Dragon> resultCollection = new ArrayList<>();
            for (Map<String, Object> map : collection) {
                resultCollection.add(buildDragon(map));
            }
            return resultCollection;
        } catch (ValueNotValidException e) {
            throw new InvalidCollectionException(e.toString());
        }
    }

//    private Dragon build(DragonData data) throws ValueNotValidException {
//        try {
//            RawDragon rawDragon = rawBuild();
//            Dragon dragon = factory.getObject(rawDragon, data.getId(), data.getCreationDate());
//            System.out.println(dragon);
//            return dragon;
//        } catch (NullPointerException e) {
//            throw new ValueNotValidException(e.toString());
//        }
//    }

//    private class DragonData {
//        @Getter
//        private Long id;
//        @Getter
//        private ZonedDateTime creationDate;
//
//        public DragonData(Long id, ZonedDateTime creationDate) {
//            this.id = id;
//            this.creationDate = creationDate;
//        }
//    }


    private Dragon buildDragon(Map<String, Object> map) throws ValueNotValidException {
        Map<String, Object> newMap = fullConvert(map, RawDragon.class);
        RawDragon rawDragon = rawBuild(map);
        long id = (long) newMap.get("id");
        ZonedDateTime creationDate = (ZonedDateTime) newMap.get("creationDate");
        return factory.getObject(rawDragon, id, creationDate);
    }

    private Coordinates buildCoords(Object object) {
        Map<String, Object> map = (Map<String, Object>) object;
        Coordinates.CoordinatesBuilder builder = Coordinates.builder();
        builder.x((Integer) map.get("x"));
        builder.y((Long) map.get("y"));
        return builder.build();
    }

    private DragonCave buildCave(Object object) {
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

    private Map<String, Object> addMissingValues(Map<String, Object> map, Class<?> target) {
        System.out.println("miss: " + map + " " + target);
        List<Field> fields = getClassFields(target);
        for (Field field : fields) {
            if (!map.containsKey(field.getName())) map.put(field.getName(), null);
        }
        System.out.println("miss: " + map + " " + target);
        return map;
    }

    private Map<Field, Object> convertMapToString(Map<Field, Object> map) throws ValueNotValidException {
        Map<Field, Object> newMap = new HashMap<>();
        for (Field field : map.keySet()) {
            if (!field.isAnnotationPresent(CollectibleField.class)) {
                newMap.put(field, convertValueToString(map.get(field)));
            } else {
                System.out.println("convert: " + field + " " + map.get(field));
                newMap.put(field, fullConvert((Map<String, Object>) map.get(field), field.getType()));
            }
        }
        return newMap;
    }

    private String convertValueToString(Object o) {
        if (o == null) return "";
        else return o.toString();
    }
}
