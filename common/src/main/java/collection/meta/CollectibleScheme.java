package collection.meta;

import collection.classes.Collectible;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;

public class CollectibleScheme implements Serializable {
    @Getter
    private final String simpleName;
    @Getter
    private final HashMap<String, FieldData> fieldsData;

    public CollectibleScheme(Class<? extends Collectible> targetClass) {
        fieldsData = new HashMap<>();
        simpleName = targetClass.getSimpleName();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            fieldsData.put(field.getName(), new FieldData(field));
        }
    }
}
