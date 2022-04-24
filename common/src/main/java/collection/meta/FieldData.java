package collection.meta;

import annotations.LowerBounded;
import annotations.NotNull;
import annotations.UserAccessible;
import collection.classes.Collectible;
import lombok.Getter;

import java.io.Serializable;
import java.lang.reflect.Field;

public class FieldData implements Serializable {
    @Getter
    private final boolean collectible;
    @Getter
    private CollectibleScheme collectibleScheme;
    @Getter
    private final boolean notNull;
    @Getter
    private final boolean userAccessible;
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
        userAccessible = field.isAnnotationPresent(UserAccessible.class);
        lowerBounded = field.isAnnotationPresent(LowerBounded.class);
        if (lowerBounded) lowerBoundedValue = field.getAnnotation(LowerBounded.class).value();
        collectible = Collectible.class.isAssignableFrom(type);
        if (collectible) {
            @SuppressWarnings({"unchecked"})
            Class<Collectible> castedType = (Class<Collectible>) type;
            collectibleScheme = new CollectibleScheme(castedType);
        }
    }
}
