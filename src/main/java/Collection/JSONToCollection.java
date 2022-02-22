package Collection;

import Collection.Classes.*;
import org.json.*;

public class JSONToCollection {
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
    public static String saveDragons(java.util.PriorityQueue<Dragon> collection) {
        java.util.PriorityQueue<Dragon> collectionCopy = new java.util.PriorityQueue<>();
        collectionCopy.addAll(collection);
        String str = "{ \"dragonCollection\": [ ";
        for (int i=0; i<=collectionCopy.size(); i++) {
            str += dragontoJSON(collectionCopy.poll()).toString();
            if (i< collectionCopy.size()) {
                str += ",";
            }
        }
        str+="]}";
        return  str;
    }
    public static JSONObject dragontoJSON(Dragon dragon) {
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
