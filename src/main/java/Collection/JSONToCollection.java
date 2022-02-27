package Collection;

import Annotations.UserAccessibleObject;
import Collection.Classes.*;
import Collection.Classes.Builders.Builder;
import Exceptions.ValueNotValidException;
import org.json.*;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.function.Function;

public class JSONToCollection {
    public static <T extends MainCollectible> PriorityQueue<MainCollectible> parseCollection(String collection, Class<T> target) throws NoSuchMethodException {
        PriorityQueue<MainCollectible> resultCollection = new PriorityQueue<>();
        JSONObject fullFile = new JSONObject(collection);
        JSONArray collectibleArray = fullFile.getJSONArray(target.getName());
        for (int i=0; i<collectibleArray.length(); i++) {
            JSONObject collectible = collectibleArray.getJSONObject(i);
            resultCollection.add(parseCollectible(collectible, target));
        }
        return resultCollection;
    }
    public static <T> T parseCollectible(JSONObject collectible, Class<T> target) throws NoSuchMethodException {
        HashMap<Field, Object> deconstructedCollectible = new HashMap<>();
        Builder builder = (Builder) target.getDeclaredMethod("getBuilder", null).invoke(null, null);
        Field[] fields = target.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType()!= Builder.class) {
                if (field.isAnnotationPresent(UserAccessibleObject.class)) {
                    //TODO maybe check if collectible instead?
                    deconstructedCollectible.put(field, parseCollectible(collectible.getJSONObject(field.getName()), field.getType()));
                } else {
                    deconstructedCollectible.put(field, getField(field, collectible));
                }
            }
        }
    }
    private static Object getField(Field field, JSONObject source) {
        String key = field.getName();
        Class<?> type = field.getType();
        HashMap<Class<?>, Function<JSONObject,?>> getters = new HashMap<>();
        getters.put(int.class, x->x.getInt(key));
        getters.put(long.class, x->x.getLong(key));
        getters.put(String.class, x->x.getString(key));
        getters.put(Long.class, x->x.getLong(key));
        getters.put(ZonedDateTime.class, x-> ZonedDateTime.parse(x.getString(key)));
        Function<JSONObject,?> getter = getters.get(type);
        if (getter!=null) {
            try {
                return getter.apply(source);
            } catch (NumberFormatException | DateTimeParseException e) {
                throw new JSONException(e.getMessage());
            }
        }
        if (field.getType().isEnum()) {
            try {
                @SuppressWarnings({"unchecked", "rawtypes"}) Object enumValue = Enum.valueOf((Class<Enum>) field.getType(), source.getString(key));
                return enumValue;
            } catch (IllegalArgumentException e) {
                throw new JSONException(e.getMessage());
            }
        }
        throw new JSONException("Couldn't parse a field");
    }
    public static java.util.PriorityQueue<Dragon> parseString(String string) throws JSONException {
        JSONObject fullFile = new JSONObject(string);
        JSONArray dragonCollection = fullFile.getJSONArray("dragonCollection");
        return JSONToCollection.parseDragons(dragonCollection);
    }
    public static java.util.PriorityQueue<Dragon> parseDragons(JSONArray dragonArray) {
        java.util.PriorityQueue<Dragon> dragons = new java.util.PriorityQueue<>();
        for (int i=0; i<dragonArray.length(); i++) {
            JSONObject dragon = dragonArray.getJSONObject(i);
            dragons.add(parseDragon(dragon));
        }
        return dragons;
    }
    public static Dragon parseDragon(JSONObject dragon) {
        long id = dragon.getLong("id");
        String name = dragon.getString("name");
        Coordinates coordinates = parseCoordinates(dragon.getJSONObject("coordinates"));
        java.time.ZonedDateTime creationDate = java.time.ZonedDateTime.parse(dragon.getString("creationDate"));
        long age = dragon.getLong("age");
        Color color = Color.valueOf(dragon.getString("color"));
        DragonType type = DragonType.valueOf(dragon.getString("type"));
        DragonCharacter character = DragonCharacter.valueOf(dragon.getString("character"));
        DragonCave cave = parseCave(dragon.getJSONObject("cave"));
        return new Dragon(id, name, coordinates, creationDate, age, color, type, character, cave);
    }
    public static Coordinates parseCoordinates(JSONObject coordinates) {
        int x = coordinates.getInt("x");
        long y = coordinates.getLong("y");
        return new Coordinates(x,y);
    }
    public static DragonCave parseCave(JSONObject cave) {
        int depth = cave.getInt("depth");
        return new DragonCave(depth);
    }
    public static String saveDragons(java.util.PriorityQueue<MainCollectible> collection) {
        java.util.PriorityQueue<MainCollectible> collectionCopy = new java.util.PriorityQueue<>();
        collectionCopy.addAll(collection);
        StringBuilder str = new StringBuilder("{ \"dragonCollection\": [ ");
        for (MainCollectible<?> dragon : collection) {
            str.append(dragonToJSON(dragon)).append(",");
        }
        str.deleteCharAt(str.length()-1);
        str.append("]}");
        return str.toString();
    }
    public static JSONObject dragonToJSON(Dragon dragon) {
        JSONObject jsonDragon = new JSONObject();
        jsonDragon.put("id", dragon.getId());
        jsonDragon.put("name", dragon.getName());
        JSONObject coordinates = JSONToCollection.coordsToJSON(dragon.getCoordinates());
        jsonDragon.put("coordinates", coordinates);
        jsonDragon.put("creationDate", dragon.getCreationDate().toString());
        jsonDragon.put("age", dragon.getAge());
        jsonDragon.put("color", dragon.getColor().toString());
        jsonDragon.put("type", dragon.getType().toString());
        jsonDragon.put("character", dragon.getCharacter().toString());
        JSONObject cave = JSONToCollection.caveToJSON(dragon.getCave());
        jsonDragon.put("cave", cave);
        return jsonDragon;
    }
    public static JSONObject coordsToJSON(Coordinates coordinates) {
        JSONObject jsonCoordinates = new JSONObject();
        jsonCoordinates.put("x", coordinates.getX());
        jsonCoordinates.put("y", coordinates.getY());
        return jsonCoordinates;
    }
    public static JSONObject caveToJSON(DragonCave cave) {
        JSONObject jsonCave = new JSONObject();
        jsonCave.put("depth", cave.getDepth());
        return  jsonCave;
    }
}
