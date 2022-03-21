package collection;

import collection.classes.MainCollectible;
import exceptions.ValueNotValidException;
import org.json.*;

import java.util.*;

/**
 * A class to convert JSOn to deconstructed collection and vise versa
 */
public class JsonHandler {

    /**
     *
     */
    public <T extends MainCollectible<T>> Collection<Map<String, Object>> parseCollection(String collection, Class<T> target) {
        List<Map<String, Object>> resultCollection = new ArrayList<>();
        JSONObject fullFile = new JSONObject(collection);
        JSONArray collectibleArray = fullFile.getJSONArray(target.getName());
        for (int i = 0; i < collectibleArray.length(); i++) {
            JSONObject collectible = collectibleArray.getJSONObject(i);
            resultCollection.add(parseCollectible(collectible));
        }
        return resultCollection;
    }

    /**
     * A method to parse a single collectible from a JSON object to a deconstructed version
     */
    public Map<String, Object> parseCollectible(JSONObject collectible) {
//        Map<String, Object> values = collectible.toMap();
//        for (String key : values.keySet()) {
//            collectionBuilder.put(key, values.get(key));
//        }
//        return collectionBuilder.build();
        return collectible.toMap();
    }


    /**
     *
     */
    public <T extends MainCollectible<T>> String serializeCollection(Collection<T> collection, Class<T> target) {
        JSONArray array = new JSONArray();
//        StringBuilder builder = new StringBuilder("{ \"" + target.getName() + "\": [");
        for (T collectible : collection) {
//            builder.append(serializeCollectible(collectible)).append(",");
            array.put(serializeCollectible(collectible));
        }
//        builder.deleteCharAt(builder.length() - 1);
//        builder.append("]}");
        JSONObject result = new JSONObject();
        result.put(target.getName(), array);
        return result.toString();
//        return builder.toString();
    }

    /**
     * A method to serialize a single deconstructed collectible into a JSON object.
     */
    public <T extends MainCollectible<T>> JSONObject serializeCollectible(T collectible) {
        return new JSONObject(collectible);
    }

}
