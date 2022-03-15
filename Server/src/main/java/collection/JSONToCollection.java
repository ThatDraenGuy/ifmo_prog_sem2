package collection;

import annotations.UserAccessibleObject;
import collection.classes.MainCollectible;
import collection.classes.builders.Builder;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class to convert JSOn to deconstructed collection and vise versa
 */
public class JSONToCollection {
    /**
     * A method to parse collection form a JSON String. Invokes {@link #parseCollectible(JSONObject, Class)} for each element in the JSON array.
     */
    public static <T extends MainCollectible> ArrayList<HashMap<Field, Object>> parseCollection(String collection, Class<T> target) {
        ArrayList<HashMap<Field, Object>> resultCollection = new ArrayList<>();
        JSONObject fullFile = new JSONObject(collection);
        JSONArray collectibleArray = fullFile.getJSONArray(target.getName());
        for (int i = 0; i < collectibleArray.length(); i++) {
            JSONObject collectible = collectibleArray.getJSONObject(i);
            resultCollection.add(parseCollectible(collectible, target));
        }
        return resultCollection;
    }

    /**
     * A method to parse a single collectible from a JSON object to a deconstructed version
     */
    private static HashMap<Field, Object> parseCollectible(JSONObject collectible, Class<?> target) {
        HashMap<Field, Object> deconstructedCollectible = new HashMap<>();
        Field[] fields = target.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() != Builder.class) {
                if (field.isAnnotationPresent(UserAccessibleObject.class) && !collectible.get(field.getName()).equals("")) {
                    //TODO maybe check if collectible instead?
                    deconstructedCollectible.put(field, parseCollectible(collectible.getJSONObject(field.getName()), field.getType()));
                } else {
                    deconstructedCollectible.put(field, collectible.getString(field.getName()));
                }
            }
        }
        return deconstructedCollectible;
    }

    /**
     * A method to serialize deconstructed collection into a JSONString. Invokes {@link #serializeCollectible(HashMap)} for each element
     */
    public static String serializeCollection(ArrayList<HashMap<Field, Object>> collection, Class<?> target) {
        StringBuilder builder = new StringBuilder("{ \"" + target.getName() + "\": [");
        for (HashMap<Field, Object> collectible : collection) {
            builder.append(serializeCollectible(collectible)).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]}");
        return builder.toString();
    }

    /**
     * A method to serialize a single deconstructed collectible into a JSON object.
     */
    private static JSONObject serializeCollectible(HashMap<Field, Object> collectible) {
        JSONObject object = new JSONObject();
        for (Field field : collectible.keySet()) {
            if (collectible.get(field) == null) {
                object.put(field.getName(), "");
            } else {
                if (field.isAnnotationPresent(UserAccessibleObject.class)) {
                    object.put(field.getName(), serializeCollectible((HashMap<Field, Object>) collectible.get(field)));
                } else {
                    object.put(field.getName(), collectible.get(field).toString());
                }
            }
        }
        return object;
    }

}
