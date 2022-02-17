package Collection;

import Collection.Classes.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSONToCollection {
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
}
