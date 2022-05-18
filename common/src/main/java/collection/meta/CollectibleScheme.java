package collection.meta;

import collection.classes.Collectible;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;

public class CollectibleScheme implements Serializable {
    @Getter
    private final String simpleName;
    @Getter
    private final HashMap<String, FieldData> fieldsData;
    @Getter
    private final Class<?> targetClass;

    public CollectibleScheme(Class<? extends Collectible> targetClass) {
        this.targetClass = targetClass;
        fieldsData = new HashMap<>();
        simpleName = targetClass.getSimpleName();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            fieldsData.put(field.getName(), new FieldData(field));
        }
    }

    @Override
    public String toString() {
        return "CollectibleScheme{" +
                "simpleName='" + simpleName + '\'' +
                ", fieldsData=" + fieldsData +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectibleScheme that = (CollectibleScheme) o;
        return simpleName.equals(that.simpleName) && fieldsData.equals(that.fieldsData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simpleName, fieldsData);
    }
}
