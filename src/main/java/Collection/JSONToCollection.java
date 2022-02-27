package Collection;

import Annotations.UserAccessibleObject;
import Collection.Classes.*;
import Collection.Classes.Builders.Builder;
import org.json.*;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class JSONToCollection {
    public static <T extends MainCollectible> ArrayList<HashMap<Field, Object>> parseCollection(String collection, Class<T> target) {
        ArrayList<HashMap<Field, Object>> resultCollection = new ArrayList<>();
        JSONObject fullFile = new JSONObject(collection);
        JSONArray collectibleArray = fullFile.getJSONArray(target.getName());
        for (int i=0; i<collectibleArray.length(); i++) {
            JSONObject collectible = collectibleArray.getJSONObject(i);
            resultCollection.add(parseCollectible(collectible, target));
        }
        return resultCollection;
    }
    private static HashMap<Field, Object> parseCollectible(JSONObject collectible, Class<?> target) {
        HashMap<Field, Object> deconstructedCollectible = new HashMap<>();
        Field[] fields = target.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType()!= Builder.class) {
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
    public static String serializeCollection(ArrayList<HashMap<Field, Object>> collection, Class<?> target) {
        StringBuilder builder = new StringBuilder("{ \""+target.getName()+"\": [");
        for (HashMap<Field, Object> collectible : collection) {
            builder.append(serializeCollectible(collectible)).append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("]}");
        return builder.toString();
    }
    private static JSONObject serializeCollectible(HashMap<Field, Object> collectible) {
        JSONObject object = new JSONObject();
        for (Field field : collectible.keySet()) {
            if (collectible.get(field)==null) {
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
