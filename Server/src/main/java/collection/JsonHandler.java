package collection;

import collection.classes.MainCollectible;
import org.json.*;
import utility.ArrayListWithID;
import utility.CollectionWithID;

import java.util.*;

/**
 * A class to convert JSOn to deconstructed collection and vise versa
 */
public class JsonHandler {

    /**
     *
     */
    public <T extends MainCollectible<T>> CollectionWithID<Map<String, Object>> parseCollection(String collection, Class<T> target) {
        JSONObject fullFile = new JSONObject(collection);
        long id = fullFile.getLong("collectionId");
        CollectionWithID<Map<String, Object>> resultCollection = new ArrayListWithID<>(id);
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
        return collectible.toMap();
    }


    /**
     *
     */
    public <T extends MainCollectible<T>> String serializeCollection(CollectionWithID<T> collection, Class<T> target) {
        JSONArray array = new JSONArray();
        for (T collectible : collection) {
            array.put(serializeCollectible(collectible));
        }

        JSONObject result = new JSONObject();
        result.put("collectionId", collection.getId());
        result.put(target.getName(), array);
        return result.toString();
    }

    /**
     * A method to serialize a single deconstructed collectible into a JSON object.
     */
    public <T extends MainCollectible<T>> JSONObject serializeCollectible(T collectible) {
        return new JSONObject(collectible);
    }

}
