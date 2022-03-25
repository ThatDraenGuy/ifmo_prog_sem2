package collection.classes;

import annotations.*;
import annotations.CollectibleField;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
public class RawDragon implements RawCollectible<Dragon> {
    @NonNull
    @NotNull
    @UserAccessible
    @Getter
    private String name; //Поле не может быть null, Строка не может быть пустой
    @NonNull
    @NotNull
    @CollectibleField
    @Getter
    @UserAccessible
    private Coordinates coordinates; //Поле не может быть null
    @UserAccessible
    @LowerBounded
    @Getter
    private Long age; //Значение поля должно быть больше 0, Поле может быть null
    @NonNull
    @NotNull
    @UserAccessible
    @Getter
    private Color color; //Поле не может быть null
    @UserAccessible
    @Getter
    private DragonType type; //Поле может быть null
    @NonNull
    @NotNull
    @UserAccessible
    @Getter
    private DragonCharacter character; //Поле не может быть null
    @CollectibleField
    @UserAccessible
    @Getter
    private DragonCave cave; //Поле может быть null
}
