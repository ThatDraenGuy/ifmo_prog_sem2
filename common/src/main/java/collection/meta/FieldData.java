package collection.meta;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserReadable;
import annotations.UserWritable;
import collection.classes.Collectible;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldData implements Serializable {
    @Getter
    private final boolean collectible;
    @Getter
    private CollectibleScheme collectibleScheme;
    @Getter
    private final boolean notNull;
    @Getter
    private final boolean userWritable;
    @Getter
    private final boolean userReadable;
    @Getter
    private final boolean lowerBounded;
    @Getter
    private double lowerBoundedValue;
    @Getter
    private final String simpleName;
    @Getter
    private final Class<?> type;

    public FieldData(Field field) {
        type = field.getType();
        simpleName = type.getSimpleName();
        notNull = field.isAnnotationPresent(NotNull.class);
        userWritable = field.isAnnotationPresent(UserWritable.class);
        userReadable = field.isAnnotationPresent(UserWritable.class) || field.isAnnotationPresent(UserReadable.class);
        lowerBounded = field.isAnnotationPresent(LowerBounded.class);
        if (lowerBounded) lowerBoundedValue = field.getAnnotation(LowerBounded.class).value();
        collectible = Collectible.class.isAssignableFrom(type);
        if (collectible) {
            @SuppressWarnings({"unchecked"})
            Class<Collectible> castedType = (Class<Collectible>) type;
            collectibleScheme = new CollectibleScheme(castedType);
        }
    }


    @Override
    public String toString() {
        return "FieldData{" +
                "collectible=" + collectible +
                ", collectibleScheme=" + collectibleScheme +
                ", notNull=" + notNull +
                ", userAccessible=" + userWritable +
                ", lowerBounded=" + lowerBounded +
                ", lowerBoundedValue=" + lowerBoundedValue +
                ", simpleName='" + simpleName + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldData fieldData = (FieldData) o;
        return collectible == fieldData.collectible && notNull == fieldData.notNull && userWritable == fieldData.userWritable &&
                lowerBounded == fieldData.lowerBounded && Double.compare(fieldData.lowerBoundedValue, lowerBoundedValue) == 0 &&
                Objects.equals(collectibleScheme, fieldData.collectibleScheme) && simpleName.equals(fieldData.simpleName) && type.equals(fieldData.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectible, collectibleScheme, notNull, userWritable, lowerBounded, lowerBoundedValue, simpleName, type);
    }
}
